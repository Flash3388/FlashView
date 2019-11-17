package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.flash3388.flashlib.time.Time;
import frc.team3388.subsystems.LiftSystem;

public class TimedLift extends Action {
    private final LiftSystem liftSystem;

    public TimedLift(Time timeout, LiftSystem liftSystem) {
        setTimeout(timeout);
        requires(liftSystem);

        this.liftSystem = liftSystem;
    }

    @Override
    protected void execute() {
        liftSystem.lift();
    }

    @Override
    protected void end() {
    }
}
