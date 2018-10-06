package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.TrayNotification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
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
                Map<String, String> hashMap = new HashMap<String, String>();
                String request = "http://34.202.9.91:8000/oth/vm/create";

                hashMap.put("name", nameField.getText());
                hashMap.put("ip", ipField.getText());
                JSONObject response = Request.post(request, hashMap);
                System.out.println(response.toString());
                if ( response.size()>2){
                    Platform.runLater(() -> TrayNotification.showNotification("VM is successfully added!"));
                    StageManager.hideAddVM();
                } else {
                    TrayNotification.showNotification("Error\nCan't add VM!\n"+response.get("error"));
                }
            }
        });
    }

}
