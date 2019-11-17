package com.flash3388.flashview.gui.icons;

import com.flash3388.flashview.gui.pane.RelocatablePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DragIcon extends RelocatablePane {

    private final ImageView mImageView;

    public DragIcon(Image image) {
        this();
        setImage(image);
    }

    public DragIcon() {
        mImageView = new ImageView();
        mImageView.setFitWidth(100);
        mImageView.setFitHeight(100);

        getChildren().add(mImageView);
    }

    public void setImage(Image image) {
        mImageView.setImage(image);
    }
}
