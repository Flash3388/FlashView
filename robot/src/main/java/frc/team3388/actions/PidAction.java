package frc.team3388.actions;

import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.robot.scheduling.actions.Action;
import com.flash3388.flashlib.robot.scheduling.Subsystem;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;

import java.util.function.DoubleSupplier;

public abstract class PidAction extends Action {
    private final PidController pidController;

    private final Time timeInThreshold;
    private final double margin;
    private final double setpoint;
    private double currentProcessValue;

    private final Clock clock;
    private Time startTime;
    private Time thresholdTime;

    public PidAction(Subsystem subsystem, PidController pidController, double margin, Time timeInThreshold, double setpoint, Clock clock) {
        if(subsystem != null)
            requires(subsystem);

        this.pidController = pidController;
        this.margin = margin;
        this.setpoint = setpoint;
        this.timeInThreshold = timeInThreshold;
        this.clock = clock;

        startTime = Time.milliseconds(0);
        thresholdTime = Time.milliseconds(0);
    }

    public PidAction(Subsystem subsystem, PidController pidController, double margin, Time timeInThreshold, Clock clock) {
        this(subsystem, pidController, margin, timeInThreshold, 0, clock);
    }

    public PidAction(PidController pidController, double margin, Time timeInThreshold, Clock clock) {
        this(null, pidController, margin, timeInThreshold, 0, clock);
    }

    @Override
    protected void initialize() {
        pidController.reset();
        resetSensors();
    }

    @Override
    protected void execute() {
        Time currentTime = clock.currentTime();
        double driveVal = 0;
        currentProcessValue = getProcessValue();

        if(isInThreshold()) {
            if (startTime.compareTo(Time.milliseconds(0)) == 0)
                startTime = currentTime;
            thresholdTime = currentTime.sub(startTime);
        }
        else {
            startTime = Time.milliseconds(0);
            driveVal = pidController.calculate(currentProcessValue,setpoint == 0 ? getSetpoint() : setpoint);
        }

        setMotorValues(driveVal);
    }

    protected abstract void setMotorValues(double pidValue);
    protected abstract double getProcessValue();
    protected void resetSensors() {

    }
    protected double getSetpoint() {
        return 0;
    }

    @Override
    protected abstract void end();

    @Override
    protected boolean isFinished() {
        return isInThreshold() && startTime.compareTo(Time.milliseconds(0)) > 0 && thresholdTime.compareTo(timeInThreshold) >= 0;
    }

    private boolean isInThreshold() {
        return ExtendedMath.constrained(currentProcessValue, setpoint - margin, setpoint + margin);
    }

    protected PidController getPidController() {
        return pidController;
    }
}
