package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.flash3388.flashlib.time.Time;
import com.google.gson.JsonObject;
import frc.team3388.actions.TimedLift;
import frc.team3388.robot.RobotMap;
import frc.team3388.subsystems.LiftSystem;

public class LiftUp implements CommandType {
    private static final String TYPE = "LiftUp";

    private final LiftSystem liftSystem;

    public LiftUp(LiftSystem liftSystem) {
        this.liftSystem = liftSystem;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new TimedLift(Time.milliseconds(RobotMap.CARGO_SHIP_BALL_LIFT_TIME),liftSystem);
    }
}
