package com.flash3388.flashview.gui;

import com.flash3388.flashview.actions.ActionType;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class MainWindow {

    private final double mWidth;
    private final double mHeight;
    private final List<ActionType> mActionTypes;

    private final AnchorPane mRoot;
    private final SplitPane mBasePane;
    private Pane mCanvasPane;
    private Control mSelectionPane;
    private DragIcon mDragItem;

    private EventHandler<DragEvent> mIconDragOverRoot = null;
    private EventHandler<DragEvent> mIconDragDropped = null;
    private EventHandler<DragEvent> mIconDragOverRightPane = null;

    public MainWindow(double width, double height, List<ActionType> actionTypes) {
        mWidth = width;
        mHeight = height;
        mActionTypes = actionTypes;

        mRoot = new AnchorPane();
        mBasePane = new SplitPane();
    }

    public Scene createScene() {
        mBasePane.setDividerPositions(0.2);
        mBasePane.setPrefSize(mWidth, mHeight);

        createLayoutDragHandlers();

        mDragItem = new DragIcon();
        mDragItem.setVisible(false);
        mDragItem.setOpacity(0.65);
        mRoot.getChildren().add(mDragItem);

        mCanvasPane = createCanvas();
        mSelectionPane = createToolBox();

        mBasePane.getItems().addAll(mSelectionPane, mCanvasPane);

        mRoot.getChildren().add(mBasePane);

        return new Scene(mRoot, mWidth, mHeight);
    }

    private Pane createCanvas() {
        AnchorPane anchorPane = new AnchorPane();

        return anchorPane;
    }

    private Control createToolBox() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefWidth(100);
        scrollPane.setPadding(new Insets(6.0, 0.0, 0.0, 8.0));

        VBox content = new VBox();
        content.setSpacing(10.0);
        scrollPane.setContent(content);

        for (ActionType actionType : mActionTypes) {
            ActionIcon actionIcon = new ActionIcon(actionType);
            addDragDetection(actionIcon);

            content.getChildren().add(actionIcon);
        }

        return scrollPane;
    }

    private void addDragDetection(ActionIcon dragIcon) {
        dragIcon.setOnDragDetected((e) -> {
            mBasePane.setOnDragOver(mIconDragOverRoot);
            mCanvasPane.setOnDragOver(mIconDragOverRightPane);
            mCanvasPane.setOnDragDropped(mIconDragDropped);

            mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            DragContainer dragContainer = new DragContainer(dragIcon.getActionType().getName());

            ClipboardContent content = new ClipboardContent();
            content.put(DragContainer.AddNode, dragContainer);

            Dragboard dragboard = mDragItem.startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(content);
            dragboard.setDragView(dragIcon.getActionType().getIcon());

            mDragItem.setVisible(true);
            mDragItem.setMouseTransparent(true);

            e.consume();
        });
    }

    private void createLayoutDragHandlers() {
        mIconDragOverRoot = (e) -> {
            Point2D point = mCanvasPane.sceneToLocal(e.getSceneX(), e.getSceneY());

            if (!mCanvasPane.boundsInLocalProperty().get().contains(point)) {
                e.acceptTransferModes(TransferMode.ANY);
                mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));
            }

            e.consume();
        };

        mIconDragOverRightPane = (e) -> {
            e.acceptTransferModes(TransferMode.ANY);
            mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            e.consume();
        };

        mIconDragDropped = (e) -> {
            DragContainer dragContainer = (DragContainer) e.getDragboard().getContent(DragContainer.AddNode);

            dragContainer.setDropCoordinates(new Point2D(e.getSceneX(), e.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            content.put(DragContainer.AddNode, dragContainer);

            e.getDragboard().setContent(content);
            e.setDropCompleted(true);
        };

        mRoot.setOnDragDone((e) -> {
            mCanvasPane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            mCanvasPane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            mBasePane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

            mDragItem.setVisible(false);

            DragContainer dragContainer = (DragContainer) e.getDragboard().getContent(DragContainer.AddNode);
            if (dragContainer != null) {
                Point2D point = dragContainer.getDropCoordinates();

                List<ActionType> actionTypes = mActionTypes.stream()
                        .filter((a) -> a.getName().equals(dragContainer.getName()))
                        .collect(Collectors.toList());

                ActionIcon actionIcon = new ActionIcon(actionTypes.get(0));
                mCanvasPane.getChildren().add(actionIcon);

                actionIcon.relocateToPoint(point);
            }

            e.consume();
        });
    }
}
