package com.flash3388.flashview.actions.types;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import com.flash3388.flashview.actions.parameters.DegreesParameter;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class RotateDegreesAction implements ActionType {

    private final Image mIcon;

    public RotateDegreesAction() {
        mIcon = null;
    }

    @Override
    public String getName() {
        return "Rotate Degrees";
    }

    @Override
    public List<ActionParameter> getParameters() {
        return Collections.singletonList(new DegreesParameter());
    }

    @Override
    public Image getIcon() {
        return mIcon;
    }
}
