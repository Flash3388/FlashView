package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.google.gson.JsonObject;

public interface CommandType {
    String getCommandType();
    Action getAction(JsonObject object);
}
