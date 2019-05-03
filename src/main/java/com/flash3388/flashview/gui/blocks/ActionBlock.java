package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import javafx.scene.image.ImageView;

import java.util.List;

public class ActionBlock extends DraggableBlock {

    private final ActionType mActionType;

    public ActionBlock(ActionType actionType) {
        mActionType = actionType;

        initView();
    }

    private void initView() {
        List<ActionParameter> parameters = mActionType.getParameters();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(mActionType.getIcon());

        addData(imageView);
    }

    public ActionType getActionType() {
        return mActionType;
    }
}
