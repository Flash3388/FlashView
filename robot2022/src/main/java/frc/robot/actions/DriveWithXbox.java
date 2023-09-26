package frc.robot.actions;

import com.flash3388.flashlib.hid.XboxAxis;
import com.flash3388.flashlib.hid.XboxController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import frc.robot.subsystems.Swerve;

public class DriveWithXbox extends ActionBase {
    private Swerve swerve;
    private XboxController xbox;

    public DriveWithXbox(Swerve swerve, XboxController xbox){
        this.swerve = swerve;
        this.xbox = xbox;

        requires(swerve);
    }

    @Override
    public void execute(ActionControl control) {
        double driveY = -xbox.getAxis(XboxAxis.LeftStickY).getAsDouble() ;
        double driveX = -xbox.getAxis(XboxAxis.LeftStickX).getAsDouble() ;
        double rotation = -xbox.getAxis(XboxAxis.RightStickX).getAsDouble();

        driveY = Math.abs(driveY) > 0.2 ? driveY : 0;
        driveX = Math.abs(driveX) > 0.2 ? driveX : 0;
        rotation = Math.abs(rotation) > 0.2 ? rotation : 0;

        this.swerve.drive(driveY * 4.4196,driveX * 4.4196,rotation * 4.4196);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
