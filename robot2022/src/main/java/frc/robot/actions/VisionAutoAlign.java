package frc.robot.actions;

import com.flash3388.flashlib.control.Direction;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.Scheduler;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.scheduling.actions.ActionConfiguration;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SystemFactory;
import frc.robot.VisionTask;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

import javax.sql.rowset.spi.SyncFactory;

public class VisionAutoAlign extends ActionBase {

    private final VisionSystem visionSystem;
    private Swerve swerve;
    private final double DISTANCE_ERROR = 0.5;


    public VisionAutoAlign(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        requires(swerve); //?
    }

    @Override
    public void execute(ActionControl actionControl) {
        double distanceX = visionSystem.getDistanceX();
        //distanceX = contourCenter.x - imageCenter.x;
        // axis x- to the right, axis y- down
        //actionControl.finish;
        if(!ExtendedMath.constrained(distanceX, -DISTANCE_ERROR, DISTANCE_ERROR)) {

            Direction rotateDirection = distanceX > 0 ? Direction.BACKWARD : Direction.FORWARD; //if - then right, if + left
            double rotation = 0.2 * rotateDirection.sign();

           /* if(Math.abs(rotation) < 0.2)
                rotation = 0.2 * Math.signum(rotation); //makes a faster rotation
            */
            swerve.drive(0, 0, -rotation);


            //swerve.drive(0, 0, -rotation * Swerve.MAX_SPEED);

        }
        else
            actionControl.finish();


        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target


    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
