package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.TODOabs.Status;
import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.ConfirmationDialog;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

public class TaskInfoController {

    private static String id = "";
    public static void setId(String ID) { id = ID; }

    @FXML
    private Text nameText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text priorityText;

    @FXML
    private Text statusText;

    @FXML
    private Text deadlineText;

    @FXML
    private ComboBox<String> statusSelect;

    @FXML
    private Button okBtn;

    @FXML
    private Button deleteBtn;

    private long deleteClickTime = 0;

    @FXML
    private void initialize() {
        TODOabs todo = Main.customer.getTODObyID(id);
        nameText.setText("Name: " + todo.getName());
        descriptionText.setText("Description: " + todo.getDescription());
        priorityText.setText("Priority: " + todo.getPriority());
        statusText.setText("Status: " + todo.getStatus());
        deadlineText.setText("Deadline: " + todo.getFinishTime());

        statusSelect.setEditable(false);
        statusSelect.getItems().addAll("CREATE", "IN_PROGRESS", "FINISH", "IMPOSSIBLE", "HAVE_QUESTIONS");

        okBtn.setOnAction(event -> {
            if(statusSelect.getValue().isEmpty() || statusSelect.getValue() == null)
                Notification.showWarningNotification("Please, choose new status");
            else {
                Main.customer.getTODObyID(id).setStatus(Status.valueOf(statusSelect.getValue()));
                statusText.setText("Status: " + statusSelect.getValue());
                Notification.showSuccessNotification("Status is successfully changed!");
            }
        });

        deleteBtn.setOnAction(event -> {
            if (ConfirmationDialog.showConfirmation("Delete task", "Are you sure want to delete this task?")) {
                if (deleteClickTime == 0 || (System.currentTimeMillis() - deleteClickTime > Main.CLICKTIME)) {
                    deleteClickTime = System.currentTimeMillis();
                    if (Main.customer.getTODObyID(id).delete() != null) {
                        Notification.showSuccessNotification("Task is successfully deleted!");
                        StageManager.hideTaskInfo();
                    } else Notification.showWarningNotification("Can't delete task!");
                }
            }
        });
    }

}