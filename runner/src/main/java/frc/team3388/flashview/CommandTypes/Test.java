package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.actions.Action;
import com.flash3388.flashlib.robot.scheduling.actions.InstantAction;

import com.google.gson.JsonObject;

import org.slf4j.Logger;

public class Test implements CommandType {
    private static final String TYPE = "Print";
    private static final String MESSAGE_KEY = "msg";

    private final Logger logger;

    public Test(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new InstantAction() {
            @Override
            protected void execute() {
                logger.info(getMessage(object));
            }
        };
    }

    private String getMessage(JsonObject object) {
        return object.get(MESSAGE_KEY).getAsString();
    }
}
