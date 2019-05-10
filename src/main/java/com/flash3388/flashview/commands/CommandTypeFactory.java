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
                    new CommandTypeBuilder("Drive", "Move Distance")
                            .addParameter(new DoubleParameterType(
                                    "Distance", "CM",
                                    Ranges.doubleRange(0.0, 100.0)))
                            .setIconPath("/MoveDistance.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("Rotate", "Rotate Degrees")
                            .addParameter(new IntegerParameterType(
                                    "Angle", "Degrees",
                                    Ranges.intRange(0, 360)))
                            .setIconPath("/rotate.jpeg")
                            .build(imageLoader),
                    new CommandTypeBuilder("LiftUp", "Lift Up")
                            .setIconPath("/lift-up.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("LiftDown", "Lift Down")
                            .setIconPath("/lift-down.jpg")
                            .build(imageLoader),
                    new CommandTypeBuilder("CargoRelease", "Release Cargo Ball")
                            .setIconPath("/ball-out.png")
                            .build(imageLoader),
                    new CommandTypeBuilder("CargoCapture", "Capture Cargo Ball")
                            .setIconPath("/ball-in.png")
                            .build(imageLoader),
                    new CommandTypeBuilder("HatchRelease", "Release Hatch Panel")
                            .setIconPath("/hatch-out.png")
                            .build(imageLoader),
                    new CommandTypeBuilder("HatchCapture", "Capture Hatch Panel")
                            .setIconPath("/hatch-in.png")
                            .build(imageLoader),
                    new CommandTypeBuilder("Stop", "Stop")
                            .addParameter(new DoubleParameterType(
                                    "WaitTime", "Seconds",
                                    Ranges.doubleRange(0.0, 10.0)))
                            .setIconPath("/stop.jpg")
                            .build(imageLoader)
            );
        } catch (IOException e) {
            throw new CommandTypeInitializationException(e);
        }
    }
}
