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
    private final double ANGLE_ERROR = 1;
    private PidController pidController;
    private final double KP = 1;
    private final double KI = 0.03;
    private final double KD = 0;
    private final double KF = 0;
    private final double PID_ERROR = 0.5;
    private final double PID_LIMIT = 2;


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
        requires(swerve);
    }

    @Override
    public void execute(ActionControl actionControl) {
        double angle2Target = visionSystem.getXAngleToTarget(); // degrees
        //distanceX = contourCenter.x - imageCenter.x;
        // axis x- to the right, axis y- down
        //actionControl.finish;
        if(!ExtendedMath.constrained(angle2Target, -ANGLE_ERROR, ANGLE_ERROR)) {

            Direction rotateDirection = angle2Target > 0 ? Direction.FORWARD : Direction.BACKWARD; //if + then right, if - left
            double rotation = 0.2 * rotateDirection.sign();

           /* if(Math.abs(rotation) < 0.2)
                rotation = 0.2 * Math.signum(rotation); //makes a faster rotation
            */
            swerve.drive(0, 0, -rotation);
            //swerve.drive(0, 0, -rotation * Swerve.MAX_SPEED);
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
