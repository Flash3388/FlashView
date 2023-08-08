package frc.robot.actions;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import frc.robot.subsystems.VisionSystem;

public class VisionAutoAlign extends ActionBase {

    private final VisionSystem visionSystem;

    public VisionAutoAlign(VisionSystem visionSystem) {
        this.visionSystem = visionSystem;
    }

    @Override
    public void execute(ActionControl actionControl) {
        double distanceX = visionSystem.getDistanceX();

        // move until distanceX is as close as possible 0,
        // indicating the robot is aligned with the target
    }
}
