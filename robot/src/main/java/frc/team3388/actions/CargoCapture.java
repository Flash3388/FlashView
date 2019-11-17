package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.actions.Action;
import frc.team3388.subsystems.CargoSystem;

public class CargoCapture extends Action {
    private final CargoSystem cargoSystem;

    public CargoCapture(CargoSystem cargoSystem) {
        requires(cargoSystem);

        this.cargoSystem = cargoSystem;
    }

    @Override
    protected void execute() {
        cargoSystem.capture();
    }

    @Override
    protected void end() {
        cargoSystem.startSlowStop();
    }

    @Override
    protected boolean isFinished() {
        return cargoSystem.isCaptured();
    }
}
