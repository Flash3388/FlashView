package frc.team3388.actions;

import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import frc.team3388.subsystems.DriveSystem;

public class PidRotate extends PidAction {
    private final DriveSystem driveSystem;

    public PidRotate(DriveSystem driveSystem, double margin, Time timeInThreshold, double setpoint, Clock clock) {
        super(driveSystem, driveSystem.getRotatePID(), margin, timeInThreshold, setpoint, clock);

        this.driveSystem = driveSystem;
    }

    @Override
    protected void initialize() {
        driveSystem.resetGyro();
    }

    @Override
    protected void end() {
        driveSystem.stop();
    }

    @Override
    protected void setMotorValues(double pidValue) {
        driveSystem.rotate(pidValue);
    }

    @Override
    protected double getProcessValue() {
        return driveSystem.getAngle();
    }

    @Override
    protected void resetSensors() {
        driveSystem.resetGyro();
    }
}
