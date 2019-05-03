package com.flash3388.flashview.gui;

import com.flash3388.flashview.actions.ActionType;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainWindow {

    private final double mWidth;
    private final double mHeight;
    private final List<ActionType> mActionTypes;

    public MainWindow(double width, double height, List<ActionType> actionTypes) {
        mWidth = width;
        mHeight = height;
        mActionTypes = actionTypes;
    }

    public Scene createScene() {
        AnchorPane absoluteRoot = new AnchorPane();

        BorderPane root = new BorderPane();

        root.setCenter(createCanvas());
        root.setLeft(createToolBox(absoluteRoot));

        absoluteRoot.getChildren().add(root);
        return new Scene(absoluteRoot, mWidth, mHeight);
    }

    private Node createCanvas() {
        Canvas canvas = new Canvas();

        return canvas;
    }

    private Node createToolBox(AnchorPane root) {
        ListView<ActionType> itemsList = new ListView<>();
        itemsList.setItems(FXCollections.observableList(mActionTypes));
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsList.setPrefWidth(200);

        itemsList.setCellFactory(param -> new ListCell<ActionType>() {
            @Override
            protected void updateItem(ActionType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(60);
                    imageView.setFitWidth(60);
                    imageView.setImage(item.getIcon());
                    setGraphic(imageView);
                }
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().add(itemsList);
        return vBox;
    }
}
