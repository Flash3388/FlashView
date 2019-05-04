package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.actions.ActionParameter;
import com.flash3388.flashview.actions.ActionType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionBlock extends DraggableBlock {

    private final ActionType mActionType;
    private final Map<ActionParameter, TextField> mParamtersfields;

    public ActionBlock(ActionType actionType) {
        mActionType = actionType;
        mParamtersfields = new HashMap<>();

        initView();
    }

    private void initView() {
        VBox totalRoot = new VBox();
        totalRoot.setSpacing(5);

        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.getChildren().add(new Label(mActionType.getName()));
        totalRoot.getChildren().add(title);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        totalRoot.getChildren().add(root);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(mActionType.getIcon());

        root.getChildren().add(imageView);

        List<ActionParameter> parameters = mActionType.getParameters();
        VBox parametersBox = new VBox();
        for (ActionParameter parameter : parameters) {
            HBox box = new HBox();
            box.setSpacing(1);

            Label label = new Label(parameter.getName());
            TextField field = new TextField();
            box.getChildren().addAll(label, field);

            parametersBox.getChildren().add(box);

            mParamtersfields.put(parameter, field);
        }

        root.getChildren().add(parametersBox);

        addData(totalRoot);
    }
}
