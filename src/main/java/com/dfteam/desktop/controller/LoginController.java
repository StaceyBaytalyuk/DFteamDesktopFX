package com.dfteam.desktop.controller;

import com.dfteam.apisdk.SDK;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        if(HomeDir.exists()) {
            try {
                if (SDK.CheckToken())
                    accountWindow();
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

                Main.customer = SDK.Auth(loginField.getText(), passwField.getText());
                if (Main.customer != null) {
                    AddFile(SDK.getToken()); // save token to file
                    accountWindow();
                }
            }
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
            fileWriter.write(json.toJSONString()); // write json to file
            fileWriter.flush(); // clean and close steam to avoid problems
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