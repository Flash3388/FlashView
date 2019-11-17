package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collections;
import java.util.List;

public class Command {

    private final CommandType mCommandType;
    private final List<CommandParameter<?>> mParameters;

    public Command(CommandType commandType, List<CommandParameter<?>> parameters) {
        mCommandType = commandType;
        mParameters = Collections.unmodifiableList(parameters);
    }

    public CommandType getCommandType() {
        return mCommandType;
    }

    public List<CommandParameter<?>> getParameters() {
        return mParameters;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(mCommandType.getName()));

        JsonObject parameters = new JsonObject();
        for (CommandParameter<?> parameter : mParameters) {
            parameters.add(parameter.getParameterType().getName(), new JsonPrimitive(parameter.getValue().toString()));
        }

        object.add("parameters", parameters);

        return object;
    }
}
