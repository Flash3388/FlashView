package frc.robot.actions.commands;

import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.auto.RotateAngle;
import frc.robot.subsystems.Swerve;

import java.util.List;

public class RotateCommand implements ActionCommandType {

    private final Swerve swerve;

    public RotateCommand(Swerve swerve) {
        this.swerve = swerve;
    }

    @Override
    public Action createAction(List<CommandParameterValue<?>> params) {
        double destAngle = (double) params.get(0).getValue();
        SmartDashboard.putNumber("destAngle", destAngle);
        return new RotateAngle(swerve, -destAngle);
    }
}
