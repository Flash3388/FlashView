package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import frc.robot.subsystems.Swerve;

import java.util.List;

public class MoveCommand implements ActionCommandType {

    private final Swerve swerve;

    public MoveCommand(Swerve swerve) {
        this.swerve = swerve;
    }

    @Override
    public Action createAction(List<CommandParameterValue<?>> params) {
        return null;
    }
}
