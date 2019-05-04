package com.flash3388.flashview.gui.icons;

import com.flash3388.flashview.commands.CommandType;
import javafx.scene.image.ImageView;

public class ActionIcon extends DragIcon {

    private final CommandType mCommandType;

    public ActionIcon(CommandType commandType) {
        mCommandType = commandType;

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setImage(mCommandType.getIcon());

        getChildren().add(imageView);
    }

    public CommandType getCommandType() {
        return mCommandType;
    }
}
