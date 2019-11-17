package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameterType;
import com.flash3388.flashview.image.ImageLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandTypeBuilder {

    private final String mName;
    private final String mDisplayName;
    private final List<CommandParameterType<?>> mParameters;
    private String mIconPath;

    public CommandTypeBuilder(String name, String displayName) {
        mName = name;
        mDisplayName = displayName;

        mParameters = new ArrayList<>();
        mIconPath = null;
    }

    public CommandTypeBuilder addParameter(CommandParameterType<?> parameter) {
        mParameters.add(parameter);
        return this;
    }

    public CommandTypeBuilder setIconPath(String path) {
        mIconPath = path;
        return this;
    }

    public CommandType build(ImageLoader imageLoader) throws IOException {
        Image image = imageLoader.loadFromResource(mIconPath);
        return new GenericCommandType(mName, mDisplayName, mParameters, image);
    }
}
