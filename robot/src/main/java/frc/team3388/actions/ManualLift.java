package frc.team3388.actions;

import com.flash3388.flashlib.robot.hid.xbox.XboxAxis;
import com.flash3388.flashlib.robot.hid.xbox.XboxController;
import com.flash3388.flashlib.robot.scheduling.actions.Action;
import frc.team3388.subsystems.LiftSystem;

public class ManualLift extends Action {
    private final LiftSystem liftSystem;
    private final XboxController xbox;

    public ManualLift(LiftSystem liftSystem, XboxController xbox) {
        requires(liftSystem);

        this.liftSystem = liftSystem;
        this.xbox = xbox;
    }

    @Override
    protected void end() {
        liftSystem.stop();
    }

    @Override
    protected void execute() {
        double leftT = xbox.getAxis(XboxAxis.LT).get();
        double rightT =xbox.getAxis(XboxAxis.RT).get();

        if(rightT > 0.1)
            liftSystem.lift();
        else if(leftT > 0.1)
            liftSystem.fall();
        else
            liftSystem.stall();
    }
}