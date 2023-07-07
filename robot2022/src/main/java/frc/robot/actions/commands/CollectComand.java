package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import frc.robot.actions.CollectCone;
import frc.robot.subsystems.Gripper;

import java.util.List;

public class CollectComand implements ActionCommandType{

    private Gripper gripper;

    public CollectComand(Gripper gripper) {
        this.gripper = gripper;
    }


    @Override
    public Action createAction(List<CommandParameterValue<?>> params) {
        return new CollectCone(gripper);
    }
}
