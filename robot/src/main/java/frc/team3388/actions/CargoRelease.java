package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.CargoSystem;

public class CargoRelease extends Action {
    private final CargoSystem cargoSystem;

    public CargoRelease(CargoSystem cargoSystem) {
        requires(cargoSystem);

        this.cargoSystem = cargoSystem;
    }

    @Override
    protected void execute() {
        cargoSystem.release();
    }

    @Override
    protected void end() {
        cargoSystem.startSlowStop();
    }

    @Override
    protected boolean isFinished() {
        return !cargoSystem.isCaptured();
    }
}
