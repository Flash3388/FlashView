package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class ActionBlock extends DragNode {

    private final ActionType mActionType;

    public ActionBlock(Pane parent, ActionType actionType) {
        super(parent);

        mActionType = actionType;

        initView();
    }

    private void initView() {
        List<ActionParameter> parameters = mActionType.getParameters();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(mActionType.getIcon());

        getChildren().add(imageView);
    }

    public ActionType getActionType() {
        return mActionType;
    }
}
