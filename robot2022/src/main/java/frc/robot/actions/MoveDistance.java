package frc.robot.actions;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.jmath.ExtendedMath;
import frc.robot.subsystems.Swerve;

public class MoveDistance extends ActionBase {

    private static final double MARGIN = 3.0;

    private final Swerve swerve;
    private final double distanceToTravelM;

    public MoveDistance(Swerve swerve, double distanceToTravelM) {
        this.swerve = swerve;
        this.distanceToTravelM = distanceToTravelM;

        requires(swerve);
    }

    @Override
    public void initialize(ActionControl control) {
        swerve.resetDistancePassed();
    }

    @Override
    public void execute(ActionControl actionControl) {
        double speedY = 1 - (swerve.getDistancePassedMeters() / distanceToTravelM);
        swerve.drive(0, speedY * Swerve.MAX_SPEED, 0);

        if (ExtendedMath.constrained(
                swerve.getDistancePassedMeters(),
                distanceToTravelM - MARGIN,
                distanceToTravelM + MARGIN)) {
            actionControl.finish();
        }
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
