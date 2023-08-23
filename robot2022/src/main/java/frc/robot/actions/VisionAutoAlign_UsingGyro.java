package frc.robot.actions;

import com.flash3388.flashlib.control.Direction;
import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class VisionAutoAlign_UsingGyro extends ActionBase{
    private final VisionSystem visionSystem;
    private final Swerve swerve;
    private double angle2Target = 0;
    private double startingAngle = 0;
    private PidController pidController;
    private final double KP = 0.3;
    private final double KI = 0;
    private final double KD = 0;
    private final double KF = 0;
    private final double PID_ERROR = 1;
    private final double PID_LIMIT = 1;
    private double deviationDistance = -6;


    public VisionAutoAlign_UsingGyro(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        startingAngle = swerve.getHeadingDegrees();
        this.angle2Target = visionSystem.getXAngleToTarget() + startingAngle  + deviationDistance;

        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);
        pidController = new PidController(RunningRobot.getControl().getClock(),
                ()-> SmartDashboard.getNumber("KP", KP),
                ()-> SmartDashboard.getNumber("KI", KI),
                ()-> SmartDashboard.getNumber("KD", KD),
                ()-> SmartDashboard.getNumber("KF", KF));
        //something
        pidController.setTolerance(PID_ERROR, Time.milliseconds(500));
        pidController.setOutputLimit(PID_LIMIT);

        configure().setName("VisionAutoAlign_UsingGyro").save();

        requires(swerve, visionSystem);
    }

    @Override
    public void initialize(ActionControl control) {
        startingAngle = swerve.getHeadingDegrees();
        this.angle2Target = visionSystem.getXAngleToTarget() + startingAngle  + deviationDistance;
        pidController.reset();
    }

    @Override
    public void execute(ActionControl actionControl) {
        //distanceX = contourCenter.x - imageCenter.x;
        // axis x- to the right, axis y- down
        //actionControl.finish;
        double gyroAngle = swerve.getHeadingDegrees();

        SmartDashboard.putNumber("gyro angle", gyroAngle);
        SmartDashboard.putNumber("starting angle", startingAngle);
        SmartDashboard.putNumber("angle2Target", angle2Target);

        if(!ExtendedMath.constrained(gyroAngle, -PID_ERROR + angle2Target, PID_ERROR + angle2Target)) {

           // Direction rotateDirection = angle2Target < startingAngle ? Direction.BACKWARD : Direction.FORWARD; //if + then right, if - left
            double rotation = pidController.applyAsDouble(gyroAngle, angle2Target) * swerve.MAX_SPEED;
            SmartDashboard.putNumber("rotation", rotation);
            swerve.drive(0, 0, -rotation);
        }
        else {
            SmartDashboard.putBoolean("got to target", true);
            actionControl.finish();
        }

        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target


    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
