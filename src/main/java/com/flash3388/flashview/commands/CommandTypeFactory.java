package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.DoubleParameterType;
import com.flash3388.flashview.commands.parameters.range.Ranges;
import com.flash3388.flashview.image.ImageLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommandTypeFactory {

    private CommandTypeFactory() { }

    public static List<CommandType> createAll(ImageLoader imageLoader) throws CommandTypeInitializationException {
        try {
            return Arrays.asList(
                    new CommandTypeBuilder("Move Distance")
                    .addParameter(new DoubleParameterType("Distance [CM]", Ranges.doubleRange(0.0, 100.0)))
                    .setIconPath("/MoveDistance.jpg")
                    .build(imageLoader)
            );
        } catch (IOException e) {
            throw new CommandTypeInitializationException(e);
        }
    }
}
