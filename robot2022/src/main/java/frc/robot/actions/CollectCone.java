package frc.robot.actions;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import frc.robot.subsystems.Gripper;

public class CollectCone extends ActionBase {
    private Gripper gripper;

    public CollectCone(Gripper gripper) {
        this.gripper=gripper;
        requires(gripper);
        configure().setTimeout(Time.seconds(1)).save();
    }

    @Override
    public void execute(ActionControl control) {
        gripper.pickCone();
    }


    @Override
    public void end(FinishReason reason) {
        gripper.stop();
    }
}
