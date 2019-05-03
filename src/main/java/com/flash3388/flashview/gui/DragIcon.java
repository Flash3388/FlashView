package com.flash3388.flashview.gui;

import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

public class DragIcon extends AnchorPane {

    public void relocateToPoint(Point2D p) {
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate(
                (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
                (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
        );
    }
}
