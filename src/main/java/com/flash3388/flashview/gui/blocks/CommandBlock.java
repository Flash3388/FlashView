package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.commands.parameters.range.ValueRange;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandBlock extends DraggableBlock {

    private final CommandType mCommandType;
    private final Map<CommandParameter, TextField> mParamtersfields;

    public CommandBlock(CommandType commandType) {
        mCommandType = commandType;
        mParamtersfields = new HashMap<>();

        initView();
    }

    private void initView() {
        VBox totalRoot = new VBox();
        totalRoot.setSpacing(5);

        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.getChildren().add(new Label(mCommandType.getName()));
        totalRoot.getChildren().add(title);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        totalRoot.getChildren().add(root);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(mCommandType.getIcon());

        root.getChildren().add(imageView);

        List<CommandParameter<?>> parameters = mCommandType.getParameters();
        VBox parametersBox = new VBox();
        for (CommandParameter<?> parameter : parameters) {
            parametersBox.getChildren().add(createForParameter(parameter));
        }

        root.getChildren().add(parametersBox);

        addData(totalRoot);
    }

    private <T> Node createForParameter(CommandParameter<T> parameter) {
        HBox box = new HBox();
        box.setSpacing(1);

        Label label = new Label(parameter.getName());
        TextField field = new TextField();
        field.focusedProperty().addListener((obs, o, n) -> {
            if (!n) {
                DataType<T> type = parameter.getValueType();
                Optional<T> optional = type.tryConvert(field.getText());

                ValueRange<T> valueRange = parameter.getValueRange();
                if (!optional.isPresent() || !valueRange.isInRange(optional.get())) {
                    // invalid input
                    field.setText("");
                }
            }
        });

        box.getChildren().addAll(label, field);

        mParamtersfields.put(parameter, field);

        return box;
    }
}
