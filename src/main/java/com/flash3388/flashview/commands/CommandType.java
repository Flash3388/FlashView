package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameter;
import javafx.scene.image.Image;

import java.util.List;

public interface CommandType {

    String getName();
    List<CommandParameter<?>> getParameters();

    Image getIcon();
}
