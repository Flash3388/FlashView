package com.flash3388.flashview.gui.icons;

import com.flash3388.flashview.commands.CommandType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ActionIcon extends DragIcon {

    private final CommandType mCommandType;

    public ActionIcon(CommandType commandType) {
        mCommandType = commandType;

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setImage(mCommandType.getIcon());

        HBox box = new HBox();
        box.setSpacing(1);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(imageView, new Label(mCommandType.getDisplayName()));

        getChildren().add(box);
    }

    public CommandType getCommandType() {
        return mCommandType;
    }
}
