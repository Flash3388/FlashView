package com.flash3388.flashview.commands;

import com.flash3388.flashview.commands.parameters.DoubleParameterType;
import com.flash3388.flashview.commands.parameters.IntegerParameterType;
import com.flash3388.flashview.commands.parameters.range.Ranges;
import com.flash3388.flashview.image.ImageLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CommandTypeFactory {

    private CommandTypeFactory() {
    }

    public static List<CommandType> createAll(ImageLoader imageLoader) throws CommandTypeInitializationException {
        try {
            return Arrays.asList(
                    new CommandTypeBuilder("Move")
                            .addParameter(new DoubleParameterType(
                                    "Distance",
                                    Ranges.doubleRange(0.0, 100.0)))
                            .setIconPath("/MoveDistance.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("Rotate")
                            .addParameter(new IntegerParameterType(
                                    "Degrees",
                                    Ranges.intRange(0, 360)))
                            .setIconPath("/rotate.jpeg")
                            .build(imageLoader),
                    new CommandTypeBuilder("LiftUp")
                            .setIconPath("/lift-up.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("LiftDown")
                            .setIconPath("/lift-down.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("BallOut")
                            .setIconPath("/ball-out.png")
                            .build(imageLoader),
                    new CommandTypeBuilder("BallIn")
                            .setIconPath("/ball-in.png")
                            .build(imageLoader)
            );
        } catch (IOException e) {
            throw new CommandTypeInitializationException(e);
        }
    }
}
