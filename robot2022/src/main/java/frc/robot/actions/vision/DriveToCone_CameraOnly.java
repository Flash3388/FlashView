package frc.robot.actions.vision;

import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class DriveToCone_CameraOnly extends ActionBase {
    private VisionSystem visionSystem;
    private Swerve swerve;

    private PidController pid;

    private static final double KP = 0.7;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private static final double ERROR = 0.03;

    private static final double PID_LIMIT = 1;

    private double setPoint;

    private Clock clock;

    private double distance;


    public DriveToCone_CameraOnly(VisionSystem visionSystem, Swerve swerve){
        this.visionSystem = visionSystem;
        this.swerve = swerve;

        this.pid = new PidController(RunningRobot.getControl().getClock(),KP, KI, KD, KF);
        pid.setOutputLimit(-PID_LIMIT, PID_LIMIT);
        pid.setTolerance(ERROR, Time.milliseconds(500));

        setPoint = visionSystem.getDistanceToTarget();

        configure().setName("DriveToCone_CameraOnly").save();

        requires(visionSystem, swerve);
    }

    @Override
    public void initialize(ActionControl control) {
        if(!visionSystem.hasTargets())
            control.finish();
        swerve.resetDistancePassed();
    }

    @Override
    public void execute(ActionControl control) {
        double speed = 1;

        if(visionSystem.hasTargets()){
            setPoint = visionSystem.getDistanceToTarget();
            swerve.resetDistancePassed();
            speed = pid.applyAsDouble(setPoint, 0);
        }

        else {
            distance = setPoint - swerve.getDistancePassedMeters();
            speed = pid.applyAsDouble(distance, 0);
        }
        SmartDashboard.putNumber("SP", setPoint);


        //swerve.drive(1, 0, 0);
        swerve.drive(-speed * swerve.MAX_SPEED,0,0);

    }

    @Override
    public boolean isFinished() {
       // double target = visionSystem.getDistanceToTarget() - 0.03; // find the units of the distance

        /*if(visionSystem.hasTargets()) // has targets
            setPoint = visionSystem.getDistanceToTarget() - 0.03;*/

       /* if(!visionSystem.hasTargets()) { // does not have any targets
            if(ExtendedMath.constrained(Math.abs(setPoint), -ERROR, ERROR)) // does not have any targets and good
                return true;

            else { // does not have any targets and bad
                setPoint = swerve.getDistancePassedMeters() + setPoint;
                return ExtendedMath.constrained(swerve.getDistancePassedMeters(), setPoint - ERROR, setPoint + ERROR);
        }*/

        if(visionSystem.hasTargets())
            return ExtendedMath.constrained(Math.abs(setPoint), -ERROR, ERROR);
        else
            return ExtendedMath.constrained(distance, -ERROR, ERROR);

       // return ExtendedMath.constrained(Math.abs(setPoint), -ERROR, ERROR); // find the units of the distance
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
