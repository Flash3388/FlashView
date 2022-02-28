package com.flash3388.flashview.gui.blocks;

import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.ViewableCommandType;
import com.flash3388.flashview.commands.data.DataType;
import com.flash3388.flashview.commands.parameters.CommandParameter;
import com.flash3388.flashview.commands.parameters.CommandParameterValue;
import com.flash3388.flashview.commands.parameters.range.ValueRange;
import com.flash3388.flashview.gui.dialogs.Dialogs;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandBlock extends DraggableBlock {

    private final Stage mOwner;
    private final ViewableCommandType mCommandType;
    private final Map<CommandParameter<?>, TextField> mParametersFields;

    public CommandBlock(Stage owner, ViewableCommandType commandType) {
        mOwner = owner;
        mCommandType = commandType;
        mParametersFields = new HashMap<>();

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
                String fieldText = field.getText();
                if (!isValidValue(parameter, fieldText)) {
                    field.setText("");
                    Dialogs.showMessageDialog(mOwner, "Invalid Value",
                            String.format("\"%s\" is not a valid value for parameter.", fieldText));
                }
            }
        });

        box.getChildren().addAll(label, field, new Label(String.format("[%s]", parameter.getMeasurementUnit())));

        mParametersFields.put(parameter, field);

        return box;
    }

    public Command toCommand() {
        List<CommandParameterValue<?>> parameters = new ArrayList<>();

        for (Map.Entry<CommandParameter<?>, TextField> entry : mParametersFields.entrySet()) {
            CommandParameter<?> type = entry.getKey();
            String value = entry.getValue().getText();

            if (!isValidValue(type, value)) {
                return null;
            }

            CommandParameterValue<?> parameter = createParameter(type, value);
            parameters.add(parameter);
        }

        return new Command(mCommandType, parameters);
    }

    private <T> boolean isValidValue(CommandParameter<T> parameter, String value) {
        DataType<T> type = parameter.getValueType();
        Optional<T> optional = type.tryConvert(value);

        ValueRange<T> valueRange = parameter.getValueRange();

        return optional.isPresent() && valueRange.isInRange(optional.get());
    }

    private <T> CommandParameterValue<T> createParameter(CommandParameter<T> type, String value) {
        Optional<T> convertedType = type.getValueType().tryConvert(value);
        return new CommandParameterValue<>(type, convertedType.get());
    }
}
