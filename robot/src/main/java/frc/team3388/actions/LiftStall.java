package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.LiftSystem;

public class LiftStall extends Action {
    private final LiftSystem liftSystem;

    public LiftStall(LiftSystem liftSystem) {
        requires(liftSystem);

        this.liftSystem = liftSystem;
    }

    @Override
    protected void execute() {
        liftSystem.stall();
    }

    @Override
    protected void end() {}
}
