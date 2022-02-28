package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameter;

import java.util.List;
import java.util.Map;

public interface CommandType {

    String getId();
    String getName();
    List<CommandParameter<?>> getParameters();

    Map<String, String> getAdditionalProperties();
}
