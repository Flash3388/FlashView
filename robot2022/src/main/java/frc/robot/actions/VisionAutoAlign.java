package frc.robot.actions;

import com.flash3388.flashlib.control.Direction;
import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class VisionAutoAlign extends ActionBase {

    private final VisionSystem visionSystem;
    private final Swerve swerve;
    private PidController pidController;
    private final double KP = 1;
    private final double KI = 0.03;
    private final double KD = 0;
    private final double KF = 0;
    private final double PID_ERROR = 2;
    private final double PID_LIMIT = 1;
    private double deviationDistance = -6;


    public VisionAutoAlign(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);
        pidController = new PidController(RunningRobot.getControl().getClock(),
                ()-> {
                    return SmartDashboard.getNumber("KP", KP);
                },
                ()-> {
                    return SmartDashboard.getNumber("KI", KI);
                },
                ()-> {
                    return SmartDashboard.getNumber("KD", KD);
                },
                ()-> {
                    return SmartDashboard.getNumber("KF", KF);
                }); //something
        pidController.setTolerance(PID_ERROR, Time.milliseconds(500));
        pidController.setOutputLimit(PID_LIMIT);

        configure().setName("VisionAutoAlign").save();

        requires(swerve);
    }

    @Override
    public void initialize(ActionControl control) {
        pidController.reset();
    }

    @Override
    public void execute(ActionControl actionControl) {
        double angle2Target = visionSystem.getXAngleToTarget() + deviationDistance; // degrees
        SmartDashboard.putNumber("angle2Target", angle2Target);
        //distanceX = contourCenter.x - imageCenter.x;
        // axis x- to the right, axis y- down
        //actionControl.finish;
        //Direction rotateDirection = angle2Target > 0 ? Direction.FORWARD : Direction.BACKWARD; //if + then right, if - left
        double rotation = pidController.applyAsDouble(angle2Target, 0) * swerve.MAX_SPEED;

           /* if(Math.abs(rotation) < 0.2)
                rotation = 0.2 * Math.signum(rotation); //makes a faster rotation
            */
        swerve.drive(0, 0, rotation);
        //swerve.drive(0, 0, -rotation * Swerve.MAX_SPEED);

        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target


    }

    @Override
    public boolean isFinished() {
        double angle2Target = visionSystem.getXAngleToTarget() + deviationDistance;
        if(ExtendedMath.constrained(angle2Target, -PID_ERROR, PID_ERROR)){
        SmartDashboard.putBoolean("got to target", true);
        return true;}
        else return false;
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
