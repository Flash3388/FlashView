package frc.robot.actions.auto;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        swerve.moveWheelsForward();
    }

    @Override
    public void execute(ActionControl actionControl) {
        double speedY = 1 - (swerve.getDistancePassedMeters() / distanceToTravelM);
        swerve.drive(speedY * Swerve.MAX_SPEED, 0, 0);


        if (Math.abs(swerve.getDistancePassedMeters()) >= distanceToTravelM) {
            actionControl.finish();
        }
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
