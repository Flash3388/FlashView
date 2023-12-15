package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import frc.robot.actions.auto.CollectCone;
import frc.robot.subsystems.Gripper;

import java.util.List;

public class CollectCommand implements ActionCommandType {

    private final Gripper gripper;

    public CollectCommand(Gripper gripper) {
        this.gripper = gripper;
    }

    @Override
    public Action createAction(List<CommandParameterValue<?>> params) {
        return new CollectCone(gripper);
    }
}


