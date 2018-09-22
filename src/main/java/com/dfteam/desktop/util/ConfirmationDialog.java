package com.dfteam.desktop.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ConfirmationDialog {
    public static boolean showConfirmation(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) return true;
        else return false;
    }
}
