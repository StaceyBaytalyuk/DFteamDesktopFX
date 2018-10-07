package com.dfteam.desktop.controller;

import com.dfteam.apisdk.Other;
import com.dfteam.apisdk.exceptions.*;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.TrayNotification;
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

    @FXML
    private void initialize() {
        addBtn.setOnAction(event -> {
            if(nameField.getText().isEmpty() || ipField.getText().isEmpty())
                TrayNotification.showNotification("Please fill all the fields");
            else {
                try {
                    Other.createVM(nameField.getText(), ipField.getText());
                    TrayNotification.showNotification("VM is successfully created!");
                    StageManager.hideAddVM();
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
                    TrayNotification.showNotification("Error\n" + e.getMessage());
                } catch (VMErrorException e) {
                    TrayNotification.showNotification("Error\nCan't create VM!\n" + e.getMessage());
                }
            }
        });
    }

}
