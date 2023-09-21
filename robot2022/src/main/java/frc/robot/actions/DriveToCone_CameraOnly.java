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

public class DriveToCone_CameraOnly extends ActionBase {
    private VisionSystem visionSystem;
    private Swerve swerve;

    private PidController pid;

    private static final double KP = 0.2;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private static final double ERROR = 0.03;


    public DriveToCone_CameraOnly(VisionSystem visionSystem, Swerve swerve){
        this.visionSystem = visionSystem;
        this.swerve = swerve;

        this.pid = new PidController(RunningRobot.getControl().getClock(),KP, KI, KD, KF);
        pid.setOutputLimit(-1, 1);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        configure().setName("DriveToCone_CameraOnly").save();

        requires(visionSystem, swerve);
    }

    @Override
    public void initialize(ActionControl control) {

    }

    @Override
    public void execute(ActionControl control) {
       /* double target = visionSystem.getDistanceToTarget() - 0.05;// find the units of the distance
        double speed = pid.applyAsDouble(target, 0);
        swerve.drive(speed,0,0);*/

        swerve.drive(2, 0, 0);
    }

    @Override
    public boolean isFinished() {
        double target = visionSystem.getDistanceToTarget() - 0.03; // find the units of the distance
        return ExtendedMath.constrained(Math.abs(target), -ERROR, ERROR); // find the units of the distance
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
