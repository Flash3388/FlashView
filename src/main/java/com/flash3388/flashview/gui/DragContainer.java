package com.flash3388.flashview.gui;

import javafx.geometry.Point2D;
import javafx.scene.input.DataFormat;

import java.io.Serializable;

public class DragContainer implements Serializable {

    public static final DataFormat AddNode =
            new DataFormat("application.DragIcon.add");

    private final String mName;
    private Point2D mDropCoordinates;

    public DragContainer(String name) {
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
