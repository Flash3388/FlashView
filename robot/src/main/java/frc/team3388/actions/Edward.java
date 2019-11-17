package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.actions.InstantAction;
import frc.team3388.subsystems.HatchSystem;

public class Edward extends InstantAction {
    private final HatchSystem hatchSystem;

    public Edward(HatchSystem hatchSystem) {
        requires(hatchSystem);

        this.hatchSystem = hatchSystem;
    }

    @Override
    protected void execute() {
        System.out.println("Toggle");
        hatchSystem.toggle();
    }
}
