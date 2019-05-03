package com.flash3388.flashview.actions.types;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import com.flash3388.flashview.actions.parameters.DistanceParameter;
import com.flash3388.flashview.gui.ActionBlock;
import com.flash3388.flashview.gui.ActionSelectionCell;
import com.flash3388.flashview.gui.ImageLoader;
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

    @Override
    public ActionSelectionCell getSelectionCell() {
        return new ActionSelectionCell(getName(), this);
    }

    @Override
    public ActionBlock getDisplayCell() {
        return null;
    }
}
