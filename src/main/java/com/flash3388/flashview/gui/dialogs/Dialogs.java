package com.flash3388.flashview.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;

public class Dialogs {

    private Dialogs() {}

    public static void showMessageDialog(Stage owner, String header, String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }

    public static void showExceptionDialog(Stage owner, String header, Throwable throwable) {
        ExceptionDialog exceptionDialog = new ExceptionDialog(throwable);
        exceptionDialog.initOwner(owner);
        exceptionDialog.setHeaderText(header);

        exceptionDialog.showAndWait();
    }
}
