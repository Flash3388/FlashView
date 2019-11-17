package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.flash3388.flashlib.robot.scheduling.actions.Actions;
import com.flash3388.flashlib.robot.scheduling.actions.InstantAction;
import com.flash3388.flashlib.time.Time;
import com.google.gson.JsonObject;

public class Stop implements CommandType {
    private static final String TYPE = "Stop";
    private static final String TIME_KEY = "WaitTime";

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        Action waitAction = Actions.empty();
        waitAction.setTimeout(Time.seconds(getTimeout(object)));

        return waitAction;
    }

    private double getTimeout(JsonObject object) {
        return object.get(TIME_KEY).getAsDouble();
    }
}
