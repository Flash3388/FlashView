package com.flash3388.flashview.gui;

import com.flash3388.flashview.actions.ActionType;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ActionDisplayCell extends AnchorPane {

    private final String mName;
    private final ActionType mActionType;

    public ActionDisplayCell(String name, ActionType actionType) {
        mName = name;
        mActionType = actionType;

        setBackground(new Background(new BackgroundFill(
                Color.web("#f68"),
                CornerRadii.EMPTY,
                Insets.EMPTY)));
    }

    public String getName() {
        return mName;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    public void relocateToPoint(Point2D p) {
        Point2D localCoords = getParent().sceneToLocal(p);
        relocate(
                (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
                (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2)));
    }
}
