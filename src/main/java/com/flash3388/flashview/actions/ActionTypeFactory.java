package com.flash3388.flashview.actions;

import com.flash3388.flashview.actions.types.MoveDistanceAction;
import com.flash3388.flashview.actions.types.RotateDegreesAction;
import com.flash3388.flashview.gui.ImageLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ActionTypeFactory {

    private ActionTypeFactory() { }

    public static List<ActionType> createAll(ImageLoader imageLoader) throws ActionTypeInitializationException {
        try {
            return Arrays.asList(
                    new MoveDistanceAction(imageLoader),
                    new RotateDegreesAction()
            );
        } catch (IOException | URISyntaxException e) {
            throw new ActionTypeInitializationException(e);
        }
    }
}
