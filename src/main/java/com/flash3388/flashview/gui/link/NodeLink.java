package com.flash3388.flashview.gui.link;

import com.flash3388.flashview.gui.blocks.DraggableBlock;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurve;

import java.util.UUID;

public class NodeLink extends AnchorPane {

    private final CubicCurve mNodeLink;

    private final DoubleProperty mControlOffsetX = new SimpleDoubleProperty();
    private final DoubleProperty mControlOffsetY = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionX1 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionY1 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionX2 = new SimpleDoubleProperty();
    private final DoubleProperty mControlDirectionY2 = new SimpleDoubleProperty();

    public NodeLink() {
        mNodeLink = new CubicCurve();

        mControlOffsetX.set(100.0);
        mControlOffsetY.set(50.0);


        mControlDirectionX1.bind(new When (
                mNodeLink.startXProperty().greaterThan(mNodeLink.endXProperty()))
                .then(-1.0).otherwise(1.0));

        mControlDirectionX2.bind(new When(
                mNodeLink.startXProperty().greaterThan(mNodeLink.endXProperty()))
                .then(1.0).otherwise(-1.0));


        mNodeLink.controlX1Property().bind(
                Bindings.add(
                        mNodeLink.startXProperty(),
                        mControlOffsetX.multiply(mControlDirectionX1)));

        mNodeLink.controlX2Property().bind(
                Bindings.add(
                        mNodeLink.endXProperty(),
                        mControlOffsetX.multiply(mControlDirectionX2)));

        mNodeLink.controlY1Property().bind(
                Bindings.add(
                        mNodeLink.startYProperty(),
                        mControlOffsetY.multiply(mControlDirectionY1)));

        mNodeLink.controlY2Property().bind(
                Bindings.add(
                        mNodeLink.endYProperty(),
                        mControlOffsetY.multiply(mControlDirectionY2)));
        
        setId(UUID.randomUUID().toString());
    }

    public void setStart(Point2D point) {
        mNodeLink.setStartX(point.getX());
        mNodeLink.setStartY(point.getY());
    }

    public void setEnd(Point2D point) {
        mNodeLink.setEndX(point.getX());
        mNodeLink.setEndY(point.getY());
    }

    public void bindEnds(DraggableBlock source, DraggableBlock target) {
        mNodeLink.startXProperty().bind(
                Bindings.add(source.layoutXProperty(), (source.getWidth() / 2.0)));

        mNodeLink.startYProperty().bind(
                Bindings.add(source.layoutYProperty(), (source.getWidth() / 2.0)));

        mNodeLink.endXProperty().bind(
                Bindings.add(target.layoutXProperty(), (target.getWidth() / 2.0)));

        mNodeLink.endYProperty().bind(
                Bindings.add(target.layoutYProperty(), (target.getWidth() / 2.0)));

        source.registerLink(getId());
        target.registerLink(getId());
    }
}
