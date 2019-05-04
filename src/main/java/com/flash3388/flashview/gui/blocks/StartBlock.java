package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.image.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class StartBlock extends DraggableBlock {

    public StartBlock(ImageLoader imageLoader) throws IOException {
        super(false, false, false);

        Image image = imageLoader.loadFromResource("/start.png");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setImage(image);

        addData(imageView);
    }
}
