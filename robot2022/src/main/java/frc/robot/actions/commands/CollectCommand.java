package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.CollectCone;
import frc.robot.actions.MoveDistance;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.Swerve;

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


