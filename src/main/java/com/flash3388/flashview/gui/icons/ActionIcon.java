package com.flash3388.flashview.gui.icons;

import com.flash3388.flashview.actions.ActionType;
import javafx.scene.image.ImageView;

public class ActionIcon extends DragIcon {

    private final ActionType mActionType;

    public ActionIcon(ActionType actionType) {
        mActionType = actionType;

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setImage(mActionType.getIcon());

        getChildren().add(imageView);
    }

    public ActionType getActionType() {
        return mActionType;
    }
}
