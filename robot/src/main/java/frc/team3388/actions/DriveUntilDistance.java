package frc.team3388.actions;

import com.beans.DoubleProperty;
import frc.team3388.subsystems.DriveSystem;

import java.util.function.DoubleSupplier;

public class DriveUntilDistance extends DriveUntil {
    private final DriveSystem driveSystem;
    private DoubleSupplier distanceSupplier;
    private final double driveSpeed;

    public DriveUntilDistance(DriveSystem driveSystem, double driveSpeed, DoubleSupplier distanceSupplier) {
        super(driveSystem, driveSpeed);

        this.driveSpeed = driveSpeed;
        this.driveSystem = driveSystem;
        this.distanceSupplier = distanceSupplier;
    }

    public DriveUntilDistance(DriveSystem driveSystem, double driveSpeed, double distance) {
        this(driveSystem,driveSpeed,()->distance);
    }

    @Override
    protected void execute() {
        driveSystem.move(driveSpeed*Math.signum(distanceSupplier.getAsDouble()));
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(driveSystem.getDistance()) > Math.abs(distanceSupplier.getAsDouble());
    }
}
