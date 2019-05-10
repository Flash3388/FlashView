package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameterType;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class GenericCommandType implements CommandType {

    private final String mName;
    private final String mDisplayName;
    private final List<CommandParameterType<?>> mParameters;
    private final Image mIcon;

    public GenericCommandType(String name, String displayName, List<CommandParameterType<?>> parameters, Image icon) {
        mName = name;
        mDisplayName = displayName;
        mParameters = Collections.unmodifiableList(parameters);
        mIcon = icon;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public List<CommandParameterType<?>> getParameters() {
        return mParameters;
    }

    @Override
    public Image getIcon() {
        return mIcon;
    }
}
