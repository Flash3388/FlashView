package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.parameters.CommandParameterType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandBlock extends DraggableBlock {

    private final CommandType mCommandType;
    private final Map<CommandParameterType<?>, TextField> mParametersFields;

    public CommandBlock(CommandType commandType) {
        mCommandType = commandType;
        mParametersFields = new HashMap<>();

        initView();
    }

    private void initView() {
        VBox totalRoot = new VBox();
        totalRoot.setSpacing(5);

        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        title.getChildren().add(new Label(mCommandType.getDisplayName()));
        totalRoot.getChildren().add(title);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        totalRoot.getChildren().add(root);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(mCommandType.getIcon());

        root.getChildren().add(imageView);

        List<CommandParameterType<?>> parameters = mCommandType.getParameters();
        VBox parametersBox = new VBox();
        for (CommandParameterType<?> parameter : parameters) {
            parametersBox.getChildren().add(createForParameter(parameter));
        }

        root.getChildren().add(parametersBox);

        addData(totalRoot);
    }

    private <T> Node createForParameter(CommandParameterType<T> parameter) {
        HBox box = new HBox();
        box.setSpacing(1);

        Label label = new Label(parameter.getName());
        TextField field = new TextField();
        field.focusedProperty().addListener((obs, o, n) -> {
            if (!n) {
                if (!isValidValue(parameter, field.getText())) {
                    field.setText("");
                }
            }
        });

        box.getChildren().addAll(label, field, new Label(String.format("[%s]", parameter.getMeasurementUnit())));

        mParametersFields.put(parameter, field);

        return box;
    }

    public Command toCommand() {
        List<CommandParameter<?>> parameters = new ArrayList<>();

        for (Map.Entry<CommandParameterType<?>, TextField> entry : mParametersFields.entrySet()) {
            CommandParameterType<?> type = entry.getKey();
            String value = entry.getValue().getText();

            if (!isValidValue(type, value)) {
                return null;
            }

            CommandParameter<?> parameter = createParameter(type, value);
            parameters.add(parameter);
        }

        return new Command(mCommandType, parameters);
    }

    private <T> boolean isValidValue(CommandParameterType<T> parameter, String value) {
        DataType<T> type = parameter.getValueType();
        Optional<T> optional = type.tryConvert(value);

        ValueRange<T> valueRange = parameter.getValueRange();

        return optional.isPresent() && valueRange.isInRange(optional.get());
    }

    private <T> CommandParameter<T> createParameter(CommandParameterType<T> type, String value) {
        Optional<T> convertedType = type.getValueType().tryConvert(value);
        return new CommandParameter<>(type, convertedType.get());
    }
}
