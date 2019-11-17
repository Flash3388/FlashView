package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.DriveSystem;

public class RotateUntil extends Action {
    private final DriveSystem driveSystem;

    private final double targetAngle;
    private final double speed;

    public RotateUntil(DriveSystem driveSystem, double targetAngle, double speed) {
        this.driveSystem = driveSystem;
        this.targetAngle = targetAngle;
        this.speed = speed;
    }

    @Override
    protected void execute() {
        driveSystem.rotate(speed);
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }

    @Override
    protected boolean isFinished() {
        return driveSystem.getAngle() > targetAngle;
    }
}
