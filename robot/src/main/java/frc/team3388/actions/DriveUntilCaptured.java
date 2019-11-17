package frc.team3388.actions;

import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;

public class DriveUntilCaptured extends DriveUntil {
    private final CargoSystem cargoSystem;

    public DriveUntilCaptured(DriveSystem driveSystem, CargoSystem cargoSystem, double driveSpeed) {
        super(driveSystem, driveSpeed);
        requires(cargoSystem);

        this.cargoSystem = cargoSystem;
    }

    @Override
    protected boolean isFinished() {
        return cargoSystem.isCaptured();
    }
}
