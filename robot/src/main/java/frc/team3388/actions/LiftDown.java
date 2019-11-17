package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.LiftSystem;

public class LiftDown extends Action {
    private final LiftSystem liftSystem;

    public LiftDown(LiftSystem liftSystem) {
        requires(liftSystem);

        this.liftSystem = liftSystem;
    }

    @Override
    protected void execute() {
        liftSystem.fall();
    }

    @Override
    protected void end() {
    }

    @Override
    protected boolean isFinished() {
        return liftSystem.isDown();
    }
}
