package frc.robot.actions.auto;

import com.flash3388.flashlib.control.Direction;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;

public class RotateAngle extends ActionBase {

    private static final double MARGIN = 0.5;

    private final Swerve swerve;
    private final double rotateByDegrees;

    private double destinationDegrees;
    private Direction rotateDirection;

    public RotateAngle(Swerve swerve, double rotateByDegrees) {
        this.swerve = swerve;
        this.rotateByDegrees = rotateByDegrees;
        configure().setName("Rotate " + rotateByDegrees).save();

        requires(swerve);
    }

    @Override
    public void initialize(ActionControl control) {
        if (Math.abs(rotateByDegrees) < 1) {
            control.finish();
            return;
        }

        destinationDegrees = swerve.getHeadingDegrees() + rotateByDegrees;
        rotateDirection = rotateByDegrees >= 0 ? Direction.FORWARD : Direction.BACKWARD;
    }

    @Override
    public void execute(ActionControl actionControl) {
        double rotation = (destinationDegrees - swerve.getHeadingDegrees()) / rotateByDegrees * rotateDirection.sign();

        if(Math.abs(rotation) < 0.2)
            rotation = 0.2 * Math.signum(rotation);

        swerve.drive(0, 0, -rotation * Swerve.MAX_SPEED);

        SmartDashboard.putBoolean("YES", false);

        if (ExtendedMath.constrained(
                swerve.getHeadingDegrees(),
                destinationDegrees - MARGIN,
                destinationDegrees + MARGIN)) {
            SmartDashboard.putBoolean("YES", true);
            actionControl.finish();
        }
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
