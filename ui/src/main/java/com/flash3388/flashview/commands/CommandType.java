package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameterType;
import javafx.scene.image.Image;

import java.util.List;

public interface CommandType {

    String getName();
    String getDisplayName();
    List<CommandParameterType<?>> getParameters();

    Image getIcon();
}
