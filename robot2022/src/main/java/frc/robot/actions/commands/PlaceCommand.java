package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import frc.robot.actions.auto.PlaceCone;
import frc.robot.subsystems.Gripper;

import java.util.List;

public class PlaceCommand implements ActionCommandType {

    private final Gripper gripper;

    public PlaceCommand(Gripper gripper) {
        this.gripper = gripper;
    }

    @Override
    public Action createAction(List<CommandParameterValue<?>> params) {
        return new PlaceCone(gripper);
    }
}
