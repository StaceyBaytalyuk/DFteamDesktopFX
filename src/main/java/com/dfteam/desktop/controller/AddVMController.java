package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.account.Account;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Class controller of addVMStage
 */
public class AddVMController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField ipField;

    @FXML
    private Button addBtn;

    private long addClickTime = 0;

    @FXML
    private void initialize() {
        addBtn.setOnAction(event -> { // add VM with chosen name and IP
            if (nameField.getText().isEmpty() || ipField.getText().isEmpty())
                Notification.showWarningNotification("Please fill all the fields");
            else {
                if (addClickTime == 0 || (System.currentTimeMillis() - addClickTime) > Main.CLICKTIME) {
                    addClickTime = System.currentTimeMillis();

                    if (new Account(" ", "oth").createVM(nameField.getText(), ipField.getText()) != null) {
//                        Other.createVM(nameField.getText(), ipField.getText());
                        Notification.showSuccessNotification("VM is successfully created!");
                        StageManager.hideAddVM();
                    } else {
                        Notification.showErrorNotification("Error\nCan't create VM!\n");
                    }
                }
            }
        });
    }

}
