package com.dfteam.desktop.controller;

import com.dfteam.apisdk.ApiSDK;
import com.dfteam.apisdk.exceptions.AuthFailException;
import com.dfteam.apisdk.exceptions.ServerNotSetException;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.Notification;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import javafx.fxml.FXML;
import java.io.*;

/**
 * Class controller of loginStage
 */
public class LoginController {

    private static File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private static File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwField;

    @FXML
    private Button btnOK;
    private long loginClickTime;

    @FXML
    private void initialize() {
        if(HomeDir.exists()){
            try {
                ApiSDK.CheckToken();
                accountWindow();
            } catch (ServerNotSetException e) {
                System.exit(1);
            } catch (AuthFailException e) {
                Notification.showErrorNotification("Error\n" + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            HomeDir.mkdir();
        }
        btnOK.setOnAction(event -> onOK());
        btnOK.setDefaultButton(true);
    }

    /**
     * btnOK handler.
     * Authorize and save token in file.
     */
    protected void onOK() {
        try {
            if (loginClickTime == 0 || (System.currentTimeMillis() - loginClickTime) > Main.CLICKTIME) {
                loginClickTime = System.currentTimeMillis();

                ApiSDK.Auth(loginField.getText(), passwField.getText());
                AddFile(ApiSDK.getToken());
                accountWindow();
            }

        } catch (AuthFailException e) {
            Notification.showErrorNotification("Wrong login or password");
        } catch (ServerNotSetException e) {
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create file with token
     */
    private void AddFile(String token){
        JSONObject json = new JSONObject();
        try {
            ConfigFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        json.put("token", token);
        try {
            FileWriter fileWriter = new FileWriter(ConfigFile);
            fileWriter.write(json.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accountWindow() throws Exception {
        StageManager.hideLogin();
        StageManager.AccountStage(new Stage());
    }

}
