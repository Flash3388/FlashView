package com.flash3388.flashview.gui;

import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    public Image loadFromResource(String resource) throws IOException {
        URL url = getClass().getResource(resource);
        return new Image(url.openStream());
    }
}
