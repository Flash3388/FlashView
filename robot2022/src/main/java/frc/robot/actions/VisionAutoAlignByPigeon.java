package frc.robot.actions;

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

public class VisionAutoAlignByPigeon extends ActionBase {

    private final VisionSystem visionSystem;
    private Swerve swerve;
    private double setPoint;
    private double currentPosition;
    private PidController pid;

    private static final double KP = 0;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private static final double ERROR = 3;

    public VisionAutoAlignByPigeon(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        this.currentPosition = swerve.getHeadingDegrees();
        this.setPoint = currentPosition + visionSystem.getXAngleToTarget();

        this.pid = new PidController(RunningRobot.getControl().getClock(),
                ()-> SmartDashboard.getNumber("KP", KP),
                ()-> SmartDashboard.getNumber("KI", KI),
                ()-> SmartDashboard.getNumber("KD", KD),
                ()-> SmartDashboard.getNumber("KF", KF));
        pid.setOutputLimit(-1, 1);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);
        SmartDashboard.putNumber("SET_POINT", setPoint);

        configure().setName("VisionAutoAlign_ByPigeon").save();

        requires(swerve, visionSystem);
    }

    @Override
    public void initialize(ActionControl control) {
        pid.reset();
    }

    @Override
    public void execute(ActionControl actionControl) {
        this.currentPosition = swerve.getHeadingDegrees();

        double rotation = this.pid.applyAsDouble(currentPosition, setPoint);
        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target

        swerve.drive(0, 0, rotation);

       /* if(ExtendedMath.constrained(currentPosition, -3, 3))
            actionControl.finish();*/
    }

    @Override
    public boolean isFinished() {
       return ExtendedMath.constrained(currentPosition, setPoint - ERROR, setPoint + ERROR);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
