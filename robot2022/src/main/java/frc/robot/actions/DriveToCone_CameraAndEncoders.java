package frc.robot.actions;

import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class DriveToCone_CameraAndEncoders extends ActionBase {
    private VisionSystem visionSystem;
    private Swerve swerve;

    private double setPoint;

    private PidController pid;

    private static final double KP = 1;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private static final double ERROR = 0.03;

    public DriveToCone_CameraAndEncoders(VisionSystem visionSystem, Swerve swerve){
        this.visionSystem = visionSystem;
        this.swerve = swerve;

        this.pid = new PidController(RunningRobot.getControl().getClock(),KP, KI, KD, KF);
        pid.setOutputLimit(-1, 1);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        this.setPoint = this.visionSystem.getDistanceToTarget() - 0.05;

        configure().setName("DriveToCone_CameraAndEncoders").save();

        requires(visionSystem, swerve);
    }


    @Override
    public void initialize(ActionControl control) {
        swerve.resetDistancePassed();
        this.setPoint = this.visionSystem.getDistanceToTarget() - 0.03;
    }

    @Override
    public void execute(ActionControl control) {
       double speed = pid.applyAsDouble(swerve.getDistancePassedMeters(), setPoint) * swerve.MAX_SPEED;
       // swerve.drive(speed, 0, 0);

        swerve.drive(2, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return ExtendedMath.constrained(swerve.getDistancePassedMeters(), setPoint - ERROR, setPoint + ERROR);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
