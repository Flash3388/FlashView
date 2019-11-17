package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.CargoSystem;

public class GripperStop extends Action {
    private final CargoSystem cargoSystem;
    private final double speedDecline;
    private final double speedMargin;

    public GripperStop(CargoSystem cargoSystem, double speedDecline, double speedMargin) {
        requires(cargoSystem);
        this.speedDecline = speedDecline;
        this.speedMargin = speedMargin;
        this.cargoSystem = cargoSystem;
    }

    @Override
    protected void execute() {
        double currentSpeed = cargoSystem.getCurrentSpeed();
        double nextSpeedAbs = Math.abs(currentSpeed) - speedDecline;

        if (nextSpeedAbs < speedMargin) {
            nextSpeedAbs = 0.0;
        }

        cargoSystem.rotate(nextSpeedAbs * Math.signum(currentSpeed));
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(cargoSystem.getCurrentSpeed()) < speedMargin;
    }

    @Override
    protected void end() {
        cargoSystem.rotate(0.0);
    }
}