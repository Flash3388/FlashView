package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;

import java.util.List;

public interface ActionCommandType {

    Action createAction(List<CommandParameterValue<?>> params);
}
