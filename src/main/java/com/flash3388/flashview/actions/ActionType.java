package com.flash3388.flashview.actions;

import javafx.scene.image.Image;

import java.util.List;

public interface ActionType {

    String getName();
    List<ActionParameter> getParameters();

    Image getIcon();
}
