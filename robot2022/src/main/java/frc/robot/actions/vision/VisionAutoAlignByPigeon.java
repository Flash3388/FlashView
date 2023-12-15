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

public class VisionAutoAlignByPigeon extends ActionBase {

    private VisionSystem visionSystem;
    private Swerve swerve;
    private double setPoint;
    private double currentPosition;
    private PidController pid;

    private static final double KP_G = 0.2;
    private static final double KI_G = 0;
    private static final double KD_G = 0;
    private static final double KF_G = 0;

    private static final double ERROR = 1;

    public VisionAutoAlignByPigeon(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        this.currentPosition = swerve.getHeadingDegrees();
        this.setPoint = this.currentPosition + visionSystem.getXAngleToTarget()  - 6;


        this.pid = new PidController(RunningRobot.getControl().getClock(),
                ()-> SmartDashboard.getNumber("KP_G", KP_G),
                ()-> SmartDashboard.getNumber("KI_G", KI_G),
                ()-> SmartDashboard.getNumber("KD_G", KD_G),
                ()-> SmartDashboard.getNumber("KF_G", KF_G));

        pid.setOutputLimit(-1, 1);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        SmartDashboard.putNumber("KP", KP_G);
        SmartDashboard.putNumber("KI", KI_G);
        SmartDashboard.putNumber("KD", KD_G);
        SmartDashboard.putNumber("KF", KF_G);


      //  SmartDashboard.putNumber("SET_POINT", setPoint);


        configure().setName("VisionAutoAlign_ByPigeon").save();

        requires(swerve, visionSystem);
    }

    @Override
    public void initialize(ActionControl control) {
        pid.reset();
        this.currentPosition = swerve.getHeadingDegrees();
        this.setPoint = this.currentPosition + visionSystem.getXAngleToTarget() - 6;
        SmartDashboard.putNumber("SET_POINT", setPoint);

        SmartDashboard.putNumber("X_ANGLE_TO_TARGET", visionSystem.getXAngleToTarget());
        SmartDashboard.putNumber("START_POINT", swerve.getHeadingDegrees());
        

    }

    @Override
    public void execute(ActionControl actionControl) {
        this.currentPosition = swerve.getHeadingDegrees();
        SmartDashboard.putNumber("CURRENT_POSITION", this.currentPosition);

        double rotation = this.pid.applyAsDouble(this.currentPosition, setPoint) * swerve.MAX_SPEED;
        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target

        swerve.drive(0, 0, -rotation);

       /* if(ExtendedMath.constrained(currentPosition, -3, 3))
            actionControl.finish();*/
    }

    @Override
    public boolean isFinished() {
       return ExtendedMath.constrained(this.currentPosition, setPoint - ERROR, setPoint + ERROR);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
