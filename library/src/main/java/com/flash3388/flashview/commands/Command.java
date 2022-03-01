package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Collections;
import java.util.List;

public class Command {

    private final CommandType mCommandType;
    private final List<CommandParameterValue<?>> mParameters;

    public Command(CommandType commandType, List<CommandParameterValue<?>> parameters) {
        mCommandType = commandType;
        mParameters = Collections.unmodifiableList(parameters);
    }

    public CommandType getCommandType() {
        return mCommandType;
    }

    public List<CommandParameterValue<?>> getParameters() {
        return mParameters;
    }
}
