package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.link.NodeLink;
import com.flash3388.flashview.gui.pane.RelocatablePane;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DraggableBlock extends RelocatablePane {

    private final AnchorPane mNextLink;
    private final Pane mData;

    private EventHandler<MouseEvent> mLinkHandleDragDetected;
    private EventHandler<DragEvent> mLinkHandleDragDropped;
    private EventHandler<DragEvent> mContextLinkDragOver;
    private EventHandler<DragEvent> mContextLinkDragDropped;

    private EventHandler<DragEvent> mContextDragOver;
    private EventHandler<DragEvent> mContextDragDropped;

    private final NodeLink mNodeLink;

    private final List<String> mLinkId;

    public DraggableBlock() {
        mData = new Pane();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-radius: 1; -fx-background-color: black;");
        getChildren().add(borderPane);

        initializeEventHandlers();
        initializeLinkDragHandlers();

        setId(UUID.randomUUID().toString());

        mLinkId = new ArrayList<>();
        mNodeLink = new NodeLink();
        mNodeLink.setVisible(false);

        Circle connector = new Circle();
        connector.setRadius(5.0);
        getChildren().add(connector);

        initializeEventHandlers();
        initializeLinkDragHandlers();

        mNextLink = createLinkPane();

        mData.setOnDragDropped(mContextDragDropped);
        mData.setOnDragOver(mContextDragOver);

        borderPane.setRight(mNextLink);
        borderPane.setCenter(mData);
    }

    public void addData(Node node) {
        mData.getChildren().add(node);
    }

    public void registerLink(String id) {
        mLinkId.add(id);
    }

    private AnchorPane createLinkPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(25.0);
        anchorPane.setOpacity(1.0);

        anchorPane.setOnDragDetected(mLinkHandleDragDetected);
        anchorPane.setOnDragDropped(mLinkHandleDragDropped);

        anchorPane.setStyle("-fx-background-color:transparent;");

        anchorPane.setOnMouseEntered((e) -> {
            anchorPane.setStyle("-fx-background-color:#dae7f3;");
            System.out.println("Hello");
        });
        anchorPane.setOnMouseExited((e) -> {
            anchorPane.setStyle("-fx-background-color:transparent;");
        });

        return anchorPane;
    }

    private void initializeEventHandlers() {
        mContextDragOver = (e) -> {
            e.acceptTransferModes(TransferMode.ANY);
            relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            e.consume();
        };

        mContextDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            e.setDropCompleted(true);

            e.consume();
        };
    }

    private void initializeLinkDragHandlers() {
        mLinkHandleDragDetected = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            getParent().setOnDragOver(mContextLinkDragOver);
            getParent().setOnDragDropped(mContextLinkDragDropped);

            Point2D p = new Point2D(
                    getLayoutX() + (getWidth() / 2.0),
                    getLayoutY() + (getHeight() / 2.0));

            mNodeLink.setVisible(false);
            mNodeLink.setStart(p);

            ClipboardContent content = new ClipboardContent();
            LinkDragContainer dragContainer = new LinkDragContainer(getId());
            content.put(DragType.ADD_LINK, dragContainer);

            Dragboard dragboard = startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(content);

            e.consume();
        };

        mLinkHandleDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            LinkDragContainer dragContainer = (LinkDragContainer) e.getDragboard().getContent(DragType.ADD_LINK);
            if (dragContainer == null) {
                return;
            }

            mNodeLink.setVisible(false);

            ClipboardContent content = new ClipboardContent();
            dragContainer.setTarget(getId());

            content.put(DragType.ADD_LINK, dragContainer);

            e.getDragboard().setContent(content);
            e.setDropCompleted(true);

            e.consume();
        };

        mContextLinkDragOver = (e) -> {
            e.acceptTransferModes(TransferMode.ANY);

            if (mNodeLink.isVisible())
                mNodeLink.setVisible(true);

            mNodeLink.setEnd(new Point2D(e.getX(), e.getY()));

            e.consume();
        };

        mContextLinkDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            mNodeLink.setVisible(true);

            e.setDropCompleted(true);

            e.consume();
        };

    }
}
