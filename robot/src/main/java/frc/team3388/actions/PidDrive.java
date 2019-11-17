package frc.team3388.actions;

import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import frc.team3388.subsystems.DriveSystem;

public class PidDrive extends PidAction {
    private final DriveSystem driveSystem;

    public PidDrive(DriveSystem driveSystem, double margin, Time timeInThreshold, double setpoint, Clock clock) {
        super(driveSystem, driveSystem.getDrivePID(), margin, timeInThreshold, setpoint, clock);

        this.driveSystem = driveSystem;
    }

    @Override
    protected void setMotorValues(double pidValue) {
        driveSystem.tankDrive(pidValue,pidValue);
    }

    @Override
    protected double getProcessValue() {
        return driveSystem.getDistance();
    }

    @Override
    protected void resetSensors() {
        driveSystem.resetEncoders();
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }
}
