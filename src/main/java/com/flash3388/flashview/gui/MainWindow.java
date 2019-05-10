package com.flash3388.flashview.gui;

import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.gui.blocks.CommandBlock;
import com.flash3388.flashview.gui.blocks.DraggableBlock;
import com.flash3388.flashview.gui.blocks.StartBlock;
import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.IconDragContainer;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.icons.ActionIcon;
import com.flash3388.flashview.gui.icons.DragIcon;
import com.flash3388.flashview.gui.link.NodeLink;
import com.flash3388.flashview.image.ImageLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class MainWindow {

    private final double mWidth;
    private final double mHeight;
    private final List<CommandType> mCommandTypes;
    private final Deployer mDeployer;

    private final AnchorPane mRoot;
    private final SplitPane mBasePane;
    private final ImageLoader mImageLoader;

    private Pane mCanvasPane;
    private Control mSelectionPane;
    private DragIcon mDragItem;
    private ScrollPane mCanvasScrollPane;

    private DraggableBlock mStartBlock;

    private EventHandler<DragEvent> mIconDragOverRoot = null;
    private EventHandler<DragEvent> mIconDragDropped = null;
    private EventHandler<DragEvent> mIconDragOverRightPane = null;

    public MainWindow(double width, double height, List<CommandType> commandTypes, Deployer deployer, ImageLoader imageLoader) {
        mWidth = width;
        mHeight = height;
        mCommandTypes = commandTypes;
        mDeployer = deployer;
        mImageLoader = imageLoader;

        mRoot = new AnchorPane();
        mBasePane = new SplitPane();
    }

    public Scene createScene() throws IOException {
        mBasePane.setDividerPositions(0.2);
        mBasePane.setPrefSize(mWidth, mHeight);
        AnchorPane.setBottomAnchor(mBasePane, 0.0);
        AnchorPane.setLeftAnchor(mBasePane, 0.0);
        AnchorPane.setRightAnchor(mBasePane, 0.0);
        AnchorPane.setTopAnchor(mBasePane, 0.0);

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
        canvasScroll.setFitToWidth(true);
        canvasScroll.setFitToHeight(true);
        mCanvasScrollPane = canvasScroll;

        BorderPane leftPane = new BorderPane();
        leftPane.setCenter(mSelectionPane);
        leftPane.setBottom(createControlsPane());

        mBasePane.getItems().addAll(leftPane, canvasScroll);
        leftPane.setMaxWidth(mWidth * 0.2);
        leftPane.setMinWidth(mWidth  * 0.2);

        mRoot.getChildren().add(mBasePane);

        mStartBlock = new StartBlock(mImageLoader);
        mCanvasPane.getChildren().add(mStartBlock);

        return new Scene(mRoot, mWidth, mHeight);
    }

    private Node createControlsPane() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);

        Button deploy = new Button("Deploy");
        deploy.setOnAction((e) -> {
            Queue<Command> commands = collectCommands();
            JsonElement serialized = serializeCommands(commands);

            try {
                mDeployer.deploy(serialized);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        box.getChildren().add(deploy);

        return box;
    }

    private Queue<Command> collectCommands() {
        Queue<Command> commands = new ArrayDeque<>();

        DraggableBlock draggableBlock = mStartBlock;
        while (draggableBlock.isConnectedToNode()) {
            draggableBlock =  draggableBlock.getConnectedNode();

            if (draggableBlock instanceof CommandBlock) {
                Command command = ((CommandBlock) draggableBlock).toCommand();
                commands.add(command);
            }
        }

        return commands;
    }

    private JsonElement serializeCommands(Queue<Command> commands) {
        JsonArray commandsArray = new JsonArray();

        for(Command command : commands) {
            commandsArray.add(command.toJson());
        }

        return commandsArray;
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

        for (CommandType commandType : mCommandTypes) {
            ActionIcon actionIcon = new ActionIcon(commandType);
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

            IconDragContainer iconDragContainer = new IconDragContainer(dragIcon.getCommandType().getDisplayName());

            ClipboardContent content = new ClipboardContent();
            content.put(DragType.ADD_NODE, iconDragContainer);

            Dragboard dragboard = mDragItem.startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(content);

            mDragItem.setImage(dragIcon.getCommandType().getIcon());
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

            if (iconDragContainer != null) {
                iconDragContainer.setDropCoordinates(new Point2D(e.getSceneX(), e.getSceneY()));

                ClipboardContent content = new ClipboardContent();
                content.put(DragType.ADD_NODE, iconDragContainer);

                e.getDragboard().setContent(content);
                e.setDropCompleted(true);
            }
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

                List<CommandType> commandTypes = mCommandTypes.stream()
                        .filter((a) -> a.getDisplayName().equals(iconDragContainer.getName()))
                        .collect(Collectors.toList());

                CommandBlock commandBlock = new CommandBlock(commandTypes.get(0));
                mCanvasPane.getChildren().add(commandBlock);
                commandBlock.relocateToPoint(point);
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
                        if (source.isConnectedToNode()) {
                            mCanvasPane.getChildren().remove(source.getConnectingLink());
                        }

                        if (target.isConnectedToNode() && target.getConnectedNode().equals(source) || !target.isCanBeAttachedTo()) {
                            System.out.println("Nope");
                        } else {
                            link.bindEnds(source, target);
                        }
                    }
                }
            }

            e.consume();
        });
    }
}
