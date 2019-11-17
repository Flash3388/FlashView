package com.flash3388.flashview.gui.drag;

import javafx.geometry.Point2D;
import javafx.scene.input.DataFormat;

import java.io.Serializable;

public class IconDragContainer implements Serializable {

    private final String mName;
    private Point2D mDropCoordinates;

    public IconDragContainer(String name) {
        mName = name;
    }

    public void setDropCoordinates(Point2D dropCoordinates) {
        mDropCoordinates = dropCoordinates;
    }

    public Point2D getDropCoordinates() {
        return mDropCoordinates;
    }

    public String getName() {
        return mName;
    }
}
