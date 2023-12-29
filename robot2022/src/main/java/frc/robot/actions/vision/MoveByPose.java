package frc.robot.actions.vision;

import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.Scheduler;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import frc.robot.subsystems.VisionSystem;
import org.photonvision.EstimatedRobotPose;

public class MoveByPose extends ActionBase {
    private final VisionSystem visionSystem;

    public MoveByPose( VisionSystem visionSystem) {
        this.visionSystem = visionSystem;
        requires(visionSystem);
    }

    @Override
    public void execute(ActionControl control) {
        Clock clock = RunningRobot.getControl().getClock();
        Time now = clock.currentTime();

        EstimatedRobotPose pose = visionSystem.getEstimatedRobotPose();
        Time estimationTime = Time.seconds(pose.timestampSeconds);
        Time timeout = Time.seconds(5);

        if(now.sub(estimationTime).largerThanOrEquals(timeout)){

        }
    }
}
