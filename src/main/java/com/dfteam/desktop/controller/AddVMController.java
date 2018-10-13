package com.dfteam.desktop.controller;

import com.dfteam.apisdk.Other;
import com.dfteam.apisdk.exceptions.*;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.Notification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;

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
        addBtn.setOnAction(event -> {
            if(nameField.getText().isEmpty() || ipField.getText().isEmpty())
                Notification.showWarningNotification("Please fill all the fields");
            else {
                try {
                    if (addClickTime == 0 || (System.currentTimeMillis() - addClickTime) > Main.CLICKTIME) {
                        addClickTime = System.currentTimeMillis();

                        Other.createVM(nameField.getText(), ipField.getText());
                        Notification.showSuccessNotification("VM is successfully created!");
                        StageManager.hideAddVM();
                    }
                } catch (ServerNotSetException e) {
                    System.out.println("Server is not set");
                    System.exit(1);
                }

                catch (InvalidTokenException e) {
                    StageManager.closeAllWindows();
                    try {
                        StageManager.LoginStage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Platform.runLater(StageManager::hideOtherVMs);
                }

                catch (AuthFailException e) {
                    Notification.showErrorNotification("Error\n" + e.getMessage());
                } catch (VMErrorException e) {
                    Notification.showErrorNotification("Error\nCan't create VM!\n" + e.getMessage());
                }
            }
        });
    }

}
