package frc.robot.actions;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SystemFactory;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

import javax.sql.rowset.spi.SyncFactory;

public class VisionAutoAlign extends ActionBase {

    private final VisionSystem visionSystem;
    private Swerve swerve;

    public VisionAutoAlign(VisionSystem visionSystem) {
        this.visionSystem = visionSystem;
        this.swerve = SystemFactory.createSwerveSystem();
    }

    @Override
    public void execute(ActionControl actionControl) {
        double distanceX = visionSystem.getDistanceX();

        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target

        swerve.drive(0, 0, 0.2); // maybe 1 - 1/distanceX
        SmartDashboard.putNumber("distanceX", distanceX);
        if(ExtendedMath.constrained(distanceX, -3, 3))
            actionControl.finish();
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
