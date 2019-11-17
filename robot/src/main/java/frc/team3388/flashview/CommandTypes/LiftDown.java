package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.google.gson.JsonObject;
import frc.team3388.subsystems.LiftSystem;

public class LiftDown implements CommandType {
    private static final String TYPE = "LiftDown";

    private final LiftSystem liftSystem;

    public LiftDown(LiftSystem liftSystem) {
        this.liftSystem = liftSystem;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new frc.team3388.actions.LiftDown(liftSystem);
    }
}
