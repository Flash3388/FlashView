package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameter;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class GenericCommandType implements CommandType {

    private final String mName;
    private final List<CommandParameter<?>> mParameters;
    private final Image mIcon;

    public GenericCommandType(String name, List<CommandParameter<?>> parameters, Image icon) {
        mName = name;
        mParameters = Collections.unmodifiableList(parameters);
        mIcon = icon;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public List<CommandParameter<?>> getParameters() {
        return mParameters;
    }

    @Override
    public Image getIcon() {
        return mIcon;
    }
}
