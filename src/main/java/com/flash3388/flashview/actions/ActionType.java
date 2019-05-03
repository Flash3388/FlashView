package com.flash3388.flashview.actions;

import com.flash3388.flashview.gui.ActionDisplayCell;
import com.flash3388.flashview.gui.ActionSelectionCell;
import javafx.scene.image.Image;

import java.util.List;

public interface ActionType {

    String getName();
    List<ActionParameter> getParameters();

    Image getIcon();

    ActionSelectionCell getSelectionCell();
    ActionDisplayCell getDisplayCell();
}
