package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateTaskController {

    private long createClickTime = 0;

    @FXML
    private TextField nameText;

    @FXML
    private TextArea descriptionText;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox prioritySelect;

    @FXML
    private Button createBtn;

    @FXML
    private void initialize() {
        prioritySelect.setEditable(false);
        prioritySelect.getItems().addAll("LOW", "MEDIUM", "HOT", "RIGHT_NOW");

        createBtn.setOnAction(event -> {
            if (createClickTime == 0 || (System.currentTimeMillis() - createClickTime) > Main.CLICKTIME) {
                createClickTime = System.currentTimeMillis();
                onCreate();
            }
        });


    }

    private void onCreate() {
        if (nameText.getText().isEmpty() || descriptionText.getText().isEmpty() ||
                nameText.getText() == null || descriptionText.getText() == null ||
                prioritySelect.getValue() == null || datePicker.getValue() == null)
            Notification.showWarningNotification("Enter all fields");
        else {
            createBtn.setDisable(false);
            if ( Main.customer.createTODO(nameText.getText(), descriptionText.getText(),
                    datePicker.getValue().toString(), TODOabs.Priority.valueOf(
                            prioritySelect.getValue().toString())) != null )
            {
                Notification.showSuccessNotification("Task is successfully created!");
            } else {
                Notification.showErrorNotification("Can't create task!");
                createBtn.setDisable(true);
            }
        }

    }

}