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

public class VisionAutoAlignByDistanceX extends ActionBase {
    private final VisionSystem visionSystem;
    private Swerve swerve;


    public VisionAutoAlignByDistanceX(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        configure().setName("DistanceX").save();
        requires(visionSystem, swerve);
    }

    @Override
    public void initialize(ActionControl control) {

    }

    @Override
    public void execute(ActionControl control) {
        double yaw = visionSystem.getXAngleToTarget();
        SmartDashboard.putNumber("yaw",yaw);
        if(!(ExtendedMath.constrained(yaw,-2,2))){
            swerve.drive(0,0,-1*Math.signum(yaw));
        }
        else{
            control.finish();
        }
    }
    /*
    @Override
    public boolean isFinished() {
        return
    }

     */

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
