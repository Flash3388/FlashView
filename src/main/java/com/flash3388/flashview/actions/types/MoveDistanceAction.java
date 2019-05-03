package com.flash3388.flashview.actions.types;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import com.flash3388.flashview.actions.parameters.DistanceParameter;
import com.flash3388.flashview.image.ImageLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class MoveDistanceAction implements ActionType {

    private final Image mIcon;

    public MoveDistanceAction(ImageLoader imageLoader) throws IOException, URISyntaxException {
        mIcon = imageLoader.loadFromResource("/MoveDistance.jpg");
    }

    @Override
    public String getName() {
        return "Move Distance";
    }

    @Override
    public List<ActionParameter> getParameters() {
        return Collections.singletonList(new DistanceParameter());
    }

    @Override
    public Image getIcon() {
        return mIcon;
    }
}
