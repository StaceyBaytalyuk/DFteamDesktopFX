package com.dfteam.desktop;

import com.dfteam.apisdk.ApiSDK;
import com.dfteam.desktop.util.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 * Main class
 * Try to open accountStage
 */
public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        ApiSDK.setServer("http://34.202.9.91:8000");

        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        if(file2.exists()) {
            try {
                FileReader fileReader = new FileReader(file2);
                JSONObject json = (JSONObject) parser.parse(fileReader);
                fileReader.close();
                ApiSDK.Auth((String) json.get("token"));
                StageManager.AccountStage(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                StageManager.LoginStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
