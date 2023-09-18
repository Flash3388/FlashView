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

    private VisionSystem visionSystem;
    private Swerve swerve;
    private double aim;
    private double rot;
    private double dirEven = 1;
    private double dirOdd = -1;


    public VisionAutoAlignByPigeon(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        aim =  swerve.getHeadingDegrees() + visionSystem.getXAngleToTarget();

        if(visionSystem.getXAngleToTarget()<0){
            rot= dirOdd;
        }
        else{
            rot = dirEven;
        }
        configure().setName("Pigeon").save();
        requires(swerve, visionSystem);
    }

    @Override
    public void initialize(ActionControl control) {
        aim =  swerve.getHeadingDegrees() + visionSystem.getXAngleToTarget();

        if(visionSystem.getXAngleToTarget()>0){
            rot = dirOdd;
        }
        else{
            rot = dirEven;
        }
    }

    @Override
    public void execute(ActionControl actionControl) {
        swerve.drive(0,0,rot);

       // actionControl.finish();
    }

    @Override
    public boolean isFinished() {
        return ExtendedMath.constrained(swerve.getHeadingDegrees(),aim-1,aim+1);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
