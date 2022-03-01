package com.flash3388.flashview.gui;

import com.flash3388.flashview.Program;
import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.ViewableCommandType;
import com.flash3388.flashview.deploy.Deployer;
import com.flash3388.flashview.gui.blocks.CommandBlock;
import com.flash3388.flashview.gui.blocks.DraggableBlock;
import com.flash3388.flashview.gui.blocks.StartBlock;
import com.flash3388.flashview.gui.dialogs.Dialogs;
import com.flash3388.flashview.gui.drag.DragType;
import com.flash3388.flashview.gui.drag.IconDragContainer;
import com.flash3388.flashview.gui.drag.LinkDragContainer;
import com.flash3388.flashview.gui.icons.ActionIcon;
import com.flash3388.flashview.gui.icons.DragIcon;
import com.flash3388.flashview.gui.link.NodeLink;
import com.flash3388.flashview.image.ImageLoader;
import com.flash3388.flashview.io.JsonProgramSaver;
import com.flash3388.flashview.io.ProgramSaver;
import com.google.gson.JsonElement;
import javafx.application.Platform;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class MainWindow {

    private static final double DIVIDOR_POSITION = 0.25;

    private final double mWidth;
    private final double mHeight;
    private final List<ViewableCommandType> mCommandTypes;
    private final Deployer mDeployer;

    private final Stage mOwner;
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

    public MainWindow(Stage owner, double width, double height, List<ViewableCommandType> commandTypes, Deployer deployer, ImageLoader imageLoader) {
        mOwner = owner;
        mWidth = width;
        mHeight = height;
        mCommandTypes = commandTypes;
        mDeployer = deployer;
        mImageLoader = imageLoader;

        mRoot = new AnchorPane();
        mBasePane = new SplitPane();
    }

    public Scene createScene() throws IOException {
        mBasePane.setDividerPositions(DIVIDOR_POSITION);
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
        leftPane.setMaxWidth(mWidth * DIVIDOR_POSITION);
        leftPane.setMinWidth(mWidth  * DIVIDOR_POSITION);

        mRoot.getChildren().add(mBasePane);

        mStartBlock = new StartBlock(mImageLoader);
        mCanvasPane.getChildren().add(mStartBlock);

        return new Scene(mRoot, mWidth, mHeight);
    }

    private Node createControlsPane() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(0.0, 0.0, 5.0, 0.0));

        Button deploy = new Button("Deploy");
        deploy.setPrefSize(100.0, 70.0);
        deploy.setOnAction((e) -> {
            deploy.setDisable(true);

            Thread deploymentThread = new Thread(()-> {
                try {
                    //mDeployer.deploy(serialized);
                    Platform.runLater(()->
                            Dialogs.showMessageDialog(mOwner, "Deployment Success",
                                    "Script deployed successfully"));
                } catch (Throwable t) {
                    t.printStackTrace();
                    Platform.runLater(()->
                            Dialogs.showExceptionDialog(mOwner, "Deployment Failed", t));
                } finally {
                    Platform.runLater(()-> deploy.setDisable(false));
                }
            }, "Deployment");

            deploymentThread.start();
        });
        deploy.setDisable(true);

        Button export = new Button("Export");
        export.setPrefSize(100.0, 70.0);
        export.setOnAction((e) -> {
            FileChooser fileChooser = new FileChooser();
            File outputFile = fileChooser.showSaveDialog(mOwner);
            if (outputFile == null) {
                return;
            }

            Program program = collectCommands();
            ProgramSaver programSaver = new JsonProgramSaver();
            try {
                programSaver.save(program, outputFile.toPath());
            } catch (Throwable t) {
                t.printStackTrace();
                Platform.runLater(()->
                        Dialogs.showExceptionDialog(mOwner, "Export Failed", t));
            }
        });

        box.setSpacing(5.0);
        box.getChildren().addAll(deploy, export);

        return box;
    }

    private Program collectCommands() {
        Queue<Command> commands = new ArrayDeque<>();

        DraggableBlock draggableBlock = mStartBlock;
        while (draggableBlock.isConnectedToNode()) {
            draggableBlock =  draggableBlock.getConnectedNode();

            if (draggableBlock instanceof CommandBlock) {
                Command command = ((CommandBlock) draggableBlock).toCommand();
                commands.add(command);
            }
        }

        return new Program(new ArrayList<>(commands));
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

        for (ViewableCommandType commandType : mCommandTypes) {
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

            IconDragContainer iconDragContainer = new IconDragContainer(dragIcon.getCommandType().getName());

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

                List<ViewableCommandType> commandTypes = mCommandTypes.stream()
                        .filter((a) -> a.getName().equals(iconDragContainer.getName()))
                        .collect(Collectors.toList());

                CommandBlock commandBlock = new CommandBlock(mOwner, commandTypes.get(0));
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
