package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.parameters.CommandTypeImpl;
import com.flash3388.flashview.image.ImageLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ViewableCommandType extends CommandTypeImpl {

    private final Image mIcon;

    public ViewableCommandType(String id, String name, List<CommandParameter<?>> parameters,
                               Map<String, String> additionalProperties, ImageLoader imageLoader) throws IOException {
        super(id, name, parameters, additionalProperties);

        String iconPath = additionalProperties.get("iconPath");
        mIcon = imageLoader.loadFromResource(iconPath);
    }

    public ViewableCommandType(CommandType other, ImageLoader imageLoader) throws IOException {
        this(other.getId(), other.getName(), other.getParameters(), other.getAdditionalProperties(), imageLoader);
    }

    public Image getIcon() {
        return mIcon;
    }
}
