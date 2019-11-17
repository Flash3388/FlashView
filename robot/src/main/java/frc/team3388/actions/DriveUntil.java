package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.DriveSystem;

public abstract class DriveUntil extends Action {
    private final DriveSystem driveSystem;

    private final double driveSpeed;

    public DriveUntil(DriveSystem driveSystem, double driveSpeed) {
        requires(driveSystem);

        this.driveSystem = driveSystem;
        this.driveSpeed = driveSpeed;
    }

    @Override
    protected void initialize() {
        driveSystem.resetEncoders();
    }

    @Override
    protected void execute() {
        driveSystem.forward(driveSpeed);
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }

    @Override
    protected abstract boolean isFinished() ;
}
