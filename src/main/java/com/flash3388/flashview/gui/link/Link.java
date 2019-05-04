package com.flash3388.flashview.gui.link;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

public class Link extends AnchorPane {

    private final Circle mCurveStart;
    private final Circle mCurveEnd;
    private final Circle mCurveC1;
    private final Circle mCurveC2;
    private final Line mStartC1;
    private final Line mC2End;

    private final CubicCurve mCurve;

    private Point2D mDragOffset;

    public Link() {
        mCurveStart = createCurveCircle();
        mCurveEnd = createCurveCircle();
        mCurveC1 = createCurveCircle();
        mCurveC2 = createCurveCircle();

        mStartC1 = createCurveLine();
        mC2End = createCurveLine();

        mCurve = new CubicCurve();
        mCurve.setControlX1(-50.0);
        mCurve.setControlX2(50.0);
        mCurve.setControlY1(-100.0);
        mCurve.setControlY2(100.0);
        mCurve.setEndX(150.0);
        mCurve.setStartX(-150.0);
        mCurve.setStroke(Color.BLACK);
        mCurve.setFill(Color.web("#1f93ff00"));

        mDragOffset = new Point2D(0.0, 0.0);

        Pane pane = new Pane();
        pane.setPrefSize(200, 200);

        Group group = new Group();
        group.getChildren().addAll(mCurveC1, mCurveC2, mCurveStart, mCurveEnd, mStartC1, mC2End, mCurve);

        Group dummyGroup = new Group();
        dummyGroup.setLayoutX(80);
        dummyGroup.setLayoutY(130);

        pane.getChildren().addAll(group, dummyGroup);


        getChildren().add(pane);

        initCurves();
    }

    public void relocateToPoint(Point2D p) {
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }

    private Circle createCurveCircle() {
        Circle circle = new Circle();
        circle.setOnMouseDragged((e) -> {
            circle.setCenterX(e.getX());
            circle.setCenterY(e.getY());
        });

        circle.setRadius(8.0);
        circle.setStroke(Color.BLACK);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.web("#2197ff"));

        return circle;
    }

    private Line createCurveLine() {
        Line line = new Line();
        line.setEndX(100.0);
        line.setStartX(-100.0);
        line.setStroke(Color.web("#d03333"));
        line.setFill(Color.web("#eb5656"));

        return line;
    }

    private void initCurves() {
        // bind control lines to circle centers
        mStartC1.startXProperty().bind(mCurve.startXProperty());
        mStartC1.startYProperty().bind(mCurve.startYProperty());

        mStartC1.endXProperty().bind(mCurve.controlX1Property());
        mStartC1.endYProperty().bind(mCurve.controlY1Property());

        mC2End.startXProperty().bind(mCurve.controlX2Property());
        mC2End.startYProperty().bind(mCurve.controlY2Property());

        mC2End.endXProperty().bind(mCurve.endXProperty());
        mC2End.endYProperty().bind(mCurve.endYProperty());

        // bind curve to circle centers
        mCurve.startXProperty().bind(mCurveStart.centerXProperty());
        mCurve.startYProperty().bind(mCurveStart.centerYProperty());

        mCurve.controlX1Property().bind(mCurveC1.centerXProperty());
        mCurve.controlY1Property().bind(mCurveC1.centerYProperty());

        mCurve.controlX2Property().bind(mCurveC2.centerXProperty());
        mCurve.controlY2Property().bind(mCurveC2.centerYProperty());

        mCurve.endXProperty().bind(mCurveEnd.centerXProperty());
        mCurve.endYProperty().bind(mCurveEnd.centerYProperty());

        mCurveStart.setCenterX(10.0f);
        mCurveStart.setCenterY(10.0f);

        mCurveC1.setCenterX(20.0f);
        //curveC1.centerXProperty().bind(Bindings.add(150.0f, curveStart.centerXProperty()));
        //curveC1.centerYProperty().bind(curveStart.centerYProperty());

        mCurveC1.setCenterX(50.0f);
        //curveC2.centerXProperty().bind(Bindings.add(-150.0f, curveEnd.centerXProperty()));
        //curveC2.centerYProperty().bind(curveEnd.centerYProperty());

        mCurveEnd.setCenterX(40.0f);
        mCurveEnd.setCenterY(40.0f);
    }
}
