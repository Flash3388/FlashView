package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.link.NodeLink;
import com.flash3388.flashview.gui.pane.RelocatablePane;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DraggableBlock extends RelocatablePane {

    private final AnchorPane mRootPane;
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
        mRootPane = new AnchorPane();
        mNextLink = new AnchorPane();
        mData = new Pane();

        initializeEventHandlers();
        initializeLinkDragHandlers();

        setId(UUID.randomUUID().toString());

        mLinkId = new ArrayList<>();
        mNodeLink = new NodeLink();
        mNodeLink.setVisible(false);
    }

    public void registerLink(String id) {
        mLinkId.add(id);
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

            getParent().setOnDragOver(mContextDragOver);
            getParent().setOnDragDropped(mContextLinkDragDropped);

            Point2D p = new Point2D(
                    getLayoutX() + (getWidth() / 2.0),
                    getLayoutY() + (getHeight() / 2.0));

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

            ClipboardContent content = new ClipboardContent();
            dragContainer.setTarget(getId());

            content.put(DragType.ADD_LINK, dragContainer);

            e.getDragboard().setContent(content);
            e.setDropCompleted(true);

            e.consume();
        };

        mContextDragOver = (e) -> {
            e.acceptTransferModes(TransferMode.ANY);

            e.consume();
        };

        mContextLinkDragDropped = (e) -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            e.setDropCompleted(true);

            e.consume();
        };
    }
}
