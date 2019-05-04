package com.flash3388.flashview.gui;

import com.flash3388.flashview.actions.ActionType;
import com.flash3388.flashview.gui.blocks.ActionBlock;
import com.flash3388.flashview.gui.blocks.DraggableBlock;
import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.icons.ActionIcon;
import com.flash3388.flashview.gui.drag.IconDragContainer;
import com.flash3388.flashview.gui.icons.DragIcon;
import com.flash3388.flashview.gui.link.NodeLink;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
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
import java.util.stream.Stream;

public class MainWindow {

    private final double mWidth;
    private final double mHeight;
    private final List<ActionType> mActionTypes;

    private final AnchorPane mRoot;
    private final SplitPane mBasePane;
    private Pane mCanvasPane;
    private Control mSelectionPane;
    private DragIcon mDragItem;
    private ScrollPane mCanvasScrollPane;

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

        ScrollPane canvasScroll = new ScrollPane();
        canvasScroll.setContent(mCanvasPane);
        canvasScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        canvasScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        canvasScroll.setPannable(true);
        canvasScroll.setFitToWidth(true);
        canvasScroll.setFitToHeight(true);
        mCanvasScrollPane = canvasScroll;

        mBasePane.getItems().addAll(mSelectionPane, canvasScroll);

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
            mCanvasScrollPane.setOnDragOver(mIconDragOverRightPane);
            mCanvasScrollPane.setOnDragDropped(mIconDragDropped);

            mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            IconDragContainer iconDragContainer = new IconDragContainer(dragIcon.getActionType().getName());

            ClipboardContent content = new ClipboardContent();
            content.put(DragType.ADD_NODE, iconDragContainer);

            Dragboard dragboard = mDragItem.startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(content);

            mDragItem.setImage(dragIcon.getActionType().getIcon());
            mDragItem.toFront();
            mDragItem.setVisible(true);
            mDragItem.setMouseTransparent(true);

            e.consume();
        });
    }

    private void createLayoutDragHandlers() {
        mIconDragOverRoot = (e) -> {
            Point2D point = mCanvasPane.sceneToLocal(e.getSceneX(), e.getSceneY());

            System.out.println("IconOver");

            if (!mCanvasPane.boundsInLocalProperty().get().contains(point)) {
                e.acceptTransferModes(TransferMode.ANY);
                mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));
            }

            e.consume();
        };

        mIconDragOverRightPane = (e) -> {
            System.out.println("IconOverR");
            e.acceptTransferModes(TransferMode.ANY);
            mDragItem.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            e.consume();
        };

        mIconDragDropped = (e) -> {
            System.out.println("IconOverD");
            IconDragContainer iconDragContainer = (IconDragContainer) e.getDragboard().getContent(DragType.ADD_NODE);

            iconDragContainer.setDropCoordinates(new Point2D(e.getSceneX(), e.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            content.put(DragType.ADD_NODE, iconDragContainer);

            e.getDragboard().setContent(content);
            e.setDropCompleted(true);
        };

        mRoot.setOnDragDone((e) -> {
            mCanvasPane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            mCanvasPane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            mCanvasScrollPane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            mCanvasScrollPane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            mBasePane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

            mDragItem.setVisible(false);

            IconDragContainer iconDragContainer = (IconDragContainer) e.getDragboard().getContent(DragType.ADD_NODE);
            if (iconDragContainer != null) {
                Point2D point = iconDragContainer.getDropCoordinates();

                List<ActionType> actionTypes = mActionTypes.stream()
                        .filter((a) -> a.getName().equals(iconDragContainer.getName()))
                        .collect(Collectors.toList());

                ActionBlock actionBlock = new ActionBlock(actionTypes.get(0));
                mCanvasPane.getChildren().add(actionBlock);
                actionBlock.relocateToPoint(point);
            }

            LinkDragContainer linkDragContainer = (LinkDragContainer) e.getDragboard().getContent(DragType.ADD_LINK);
            if (linkDragContainer != null) {
                String sourceId = linkDragContainer.getId();
                String targetId = linkDragContainer.getTarget();

                if (sourceId != null && targetId != null) {
                    NodeLink link = new NodeLink();
                    mCanvasPane.getChildren().add(0, link);

                    DraggableBlock source = null;
                    DraggableBlock target = null;

                    for (Node node : mCanvasPane.getChildren()) {
                        if (sourceId.equals(node.getId())) {
                            source = (DraggableBlock) node;
                        }

                        if (targetId.equals(node.getId())) {
                            target = (DraggableBlock) node;
                        }
                    }

                    if (source != null && target != null) {
                        link.bindEnds(source, target);
                    }
                }
            }

            e.consume();
        });
    }
}
