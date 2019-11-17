package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;

public class DriveAndCapture extends Action {
    private final DriveSystem driveSystem;
    private final CargoSystem cargoSystem;
    private final double driveSpeed;

    public DriveAndCapture(DriveSystem driveSystem, CargoSystem cargoSystem, double driveSpeed) {
        requires(driveSystem, cargoSystem);

        this.driveSystem = driveSystem;
        this.cargoSystem = cargoSystem;
        this.driveSpeed = driveSpeed;
    }

    @Override
    protected void execute() {
        driveSystem.tankDrive(driveSpeed,driveSpeed);
        cargoSystem.capture();
    }

    @Override
    protected void end() {
        driveSystem.stop();
        cargoSystem.startSlowStop();
    }

    @Override
    protected boolean isFinished() {
        return cargoSystem.isCaptured();
    }
}
