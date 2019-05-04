package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.gui.drag.BlockDragContainer;
import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.link.NodeLink;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DraggableBlock extends AnchorPane {

    private final AnchorPane mNextLink;
    private final AnchorPane mPrevLink;
    private final Pane mData;

    private EventHandler<MouseEvent> mLinkHandleDragDetected;
    private EventHandler<DragEvent> mLinkHandleDragDropped;
    private EventHandler<DragEvent> mContextLinkDragOver;
    private EventHandler<DragEvent> mContextLinkDragDropped;

    private EventHandler<DragEvent> mContextDragOver;
    private EventHandler<DragEvent> mContextDragDropped;

    private Point2D mDragOffset;
    private AnchorPane mParentPane;

    private final NodeLink mNodeLink;

    private final AtomicReference<DraggableBlock> mConnectedBlock;
    private final AtomicReference<NodeLink> mConnectingLink;

    public DraggableBlock() {
        mData = new Pane();

        mDragOffset = new Point2D(0.0, 0.0);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-radius: 1; -fx-background-color: black;");
        getChildren().add(borderPane);

        initializeEventHandlers();
        initializeLinkDragHandlers();

        setId(UUID.randomUUID().toString());

        mConnectedBlock = new AtomicReference<>();
        mConnectingLink = new AtomicReference<>();
        mNodeLink = new NodeLink();
        mNodeLink.setVisible(false);

        Circle connector = new Circle();
        connector.setRadius(5.0);
        getChildren().add(connector);

        mNextLink = createLinkPane();
        mPrevLink = new AnchorPane();//createLinkPane();
        mPrevLink.setOnDragDropped(mLinkHandleDragDropped);
        mPrevLink.setPrefWidth(25.0);
        mPrevLink.setOpacity(1.0);

        mData.setOnDragDetected((e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragOver(null);
            getParent().getParent().setOnDragDropped(null);

            getParent().setOnDragOver(mContextDragOver);
            getParent().setOnDragDropped(mContextDragDropped);
            getParent().getParent().setOnDragOver(mContextDragOver);
            getParent().getParent().setOnDragDropped(mContextDragDropped);

            System.out.println("DDD");

            mDragOffset = new Point2D(e.getX(), e.getY());

            relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            BlockDragContainer dragContainer = new BlockDragContainer();
            content.put(DragType.DRAG_NODE, dragContainer);

            Dragboard dragboard = startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(content);

            e.consume();
        });

        borderPane.setRight(mNextLink);
        borderPane.setLeft(mPrevLink);
        borderPane.setCenter(mData);

        parentProperty().addListener((obs, o, n) -> {
            mParentPane = (AnchorPane) getParent();
        });
    }

    public void addData(Node node) {
        mData.getChildren().add(node);
    }

    public void registerNextNode(DraggableBlock block, NodeLink connectingLink) {
        mConnectedBlock.set(block);
        mConnectingLink.set(connectingLink);
    }

    public NodeLink getConnectingLink() {
        return mConnectingLink.get();
    }

    public boolean isConnectedToNode() {
        return mConnectedBlock.get() != null;
    }

    public DraggableBlock getConnectedNode() {
        return mConnectedBlock.get();
    }

    public void relocateToPoint(Point2D p) {
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
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

            //System.out.println("Over");

            e.consume();
        };

        mContextDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragOver(null);
            getParent().getParent().setOnDragDropped(null);

            e.setDropCompleted(true);

            e.consume();
        };
    }

    private void initializeLinkDragHandlers() {
        mLinkHandleDragDetected = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragOver(null);

            getParent().setOnDragOver(mContextLinkDragOver);
            getParent().setOnDragDropped(mContextLinkDragDropped);
            getParent().getParent().setOnDragDropped(mContextLinkDragDropped);
            getParent().getParent().setOnDragOver(mContextLinkDragOver);

            mParentPane.getChildren().add(0, mNodeLink);

            Point2D p = new Point2D(
                    getLayoutX() + (getWidth() / 2.0),
                    getLayoutY() + (getHeight() / 2.0));

            mNodeLink.setVisible(false);
            mNodeLink.setStart(p);

            System.out.println("Drag link: " + getId());

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
            getParent().getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragOver(null);

            LinkDragContainer dragContainer = (LinkDragContainer) e.getDragboard().getContent(DragType.ADD_LINK);
            if (dragContainer == null) {
                return;
            }

            System.out.println("Drag link dropped: " + getId());

            mNodeLink.setVisible(false);
            mParentPane.getChildren().remove(0);

            ClipboardContent content = new ClipboardContent();
            dragContainer.setTarget(getId());

            content.put(DragType.ADD_LINK, dragContainer);

            e.getDragboard().setContent(content);
            e.setDropCompleted(true);

            e.consume();
        };

        mContextLinkDragOver = (e) -> {
            e.acceptTransferModes(TransferMode.ANY);

            if (!mNodeLink.isVisible())
                mNodeLink.setVisible(true);

            mNodeLink.setEnd(new Point2D(e.getX(), e.getY()));

            e.consume();
        };

        mContextLinkDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragDropped(null);
            getParent().getParent().setOnDragOver(null);

            mNodeLink.setVisible(false);
            mParentPane.getChildren().remove(0);

            e.setDropCompleted(true);

            e.consume();
        };

    }
}
