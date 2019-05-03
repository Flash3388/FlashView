package com.flash3388.flashview.gui.blocks;

import javafx.scene.layout.Pane;

public class DragNode extends Pane {

    private final Pane mParent;

    private double mX;
    private double mY;
    private double mMouseX;
    private double mMouseY;

    private boolean mIsDragging;
    private boolean mShouldMoveToFront;

    public DragNode(Pane parent) {
        mParent = parent;

        mX = 0.0;
        mY = 0.0;
        mMouseX = 0.0;
        mMouseY = 0.0;
        mIsDragging = false;
        mShouldMoveToFront = false;

        initDrag();
    }

    private void initDrag() {
        onMousePressedProperty().set((e) -> {
            mMouseX = e.getSceneX();
            mMouseY = e.getSceneY();

            mX = getLayoutX();
            mY = getLayoutY();

            if (isShouldMoveToFront()) {
                toFront();
            }
        });

        onMouseDraggedProperty().set((e) -> {
            double xOffset = e.getSceneX() - mMouseX;
            double yOffset = e.getSceneY() - mMouseY;

            mX += xOffset;
            mY += yOffset;

            setLayoutX(mX);
            setLayoutY(mY);

            mIsDragging = true;

            mMouseX = e.getSceneX();
            mMouseY = e.getSceneY();

            e.consume();
        });

        onMouseClickedProperty().set((e) -> {
            mIsDragging = false;
        });
    }

    public boolean isDragging() {
        return mIsDragging;
    }

    public boolean isShouldMoveToFront() {
        return mShouldMoveToFront;
    }

    public void removeFromParent() {
        mParent.getChildren().remove(this);
    }
}
