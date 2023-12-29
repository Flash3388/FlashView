package frc.robot.actions.vision;

import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class VisionAutoAlignByDistanceX extends ActionBase {
    private final VisionSystem visionSystem;
    private Swerve swerve;
    private double distanceX;
    private PidController pid;

    private static final double KP = 0.2;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private static final double ERROR = 2;

    private static final double SET_POINT = 0;

    public VisionAutoAlignByDistanceX(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.distanceX = visionSystem.getXAngleToTarget() - 6;
        this.swerve = swerve;

        this.pid = new PidController(RunningRobot.getControl().getClock(),
                ()-> SmartDashboard.getNumber("KP_D", KP),
                ()-> SmartDashboard.getNumber("KI_D", KI),
                ()-> SmartDashboard.getNumber("KD_D", KD),
                ()-> SmartDashboard.getNumber("KF_D", KF));
        pid.setOutputLimit(-1, 1);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        SmartDashboard.putNumber("KP_D", KP);
        SmartDashboard.putNumber("KI_D", KI);
        SmartDashboard.putNumber("KD_D", KD);
        SmartDashboard.putNumber("KF_D", KF);

        configure().setName("VisionAutoAlign_ByDistanceX").save();

        requires(swerve, visionSystem);
    }

    @Override
    public void initialize(ActionControl control) {
        pid.reset();
        distanceX = visionSystem.getXAngleToTarget();
    }

    @Override
    public void execute(ActionControl control) {
        SmartDashboard.putNumber("DISTANCE_X", distanceX);
        this.distanceX = visionSystem.getXAngleToTarget() - 6;
        double rotation = pid.applyAsDouble(distanceX, SET_POINT) * swerve.MAX_SPEED ;
        swerve.drive(0,0,rotation, false);
    }

    @Override
    public boolean isFinished() {
        return ExtendedMath.constrained(distanceX, -ERROR, ERROR);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
