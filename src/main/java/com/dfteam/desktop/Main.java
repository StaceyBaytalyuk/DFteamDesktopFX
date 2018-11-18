package com.dfteam.desktop;

import com.dfteam.apisdk.SDK;
import com.dfteam.apisdk.util.Customer;
import com.dfteam.apisdk.util.ExceptionController;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.application.Application;
import javafx.application.Platform;
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
public class Main extends Application {

    public final static String url = "http://35.204.138.143:8000";
    public final static long CLICKTIME = 2000; // milliseconds between clicks
    public static Customer customer = null;

    @Override
    public void start(Stage primaryStage) {
        SDK.initialExceptions(new ExceptionController() {
            @Override
            public void onServerNotSet(Exception e) {
                System.out.println("Server is not set");
                Platform.exit();
                System.exit(1);
            }

            @Override
            public void onAuthFailError(Exception e) {
                Notification.showWarningNotification("Please, sign in");
            }

            @Override
            public void onInvalidTokenError(Exception e) {

                StageManager.closeAllWindows();
                try {
                    StageManager.LoginStage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onAccountError(Exception e) {
                Notification.showErrorNotification("Error\n" + e.getMessage());
            }

            @Override
            public void onVMError(Exception e) {
                Notification.showErrorNotification("Error\n" + e.getMessage());
            }
        });

        SDK.setServer(url);

        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        if(file2.exists()) {
            try {
                FileReader fileReader = new FileReader(file2);
                JSONObject json = (JSONObject) parser.parse(fileReader); // read from file to JSONObject
                fileReader.close();
                customer = SDK.Auth((String) json.get("token"));
                if(customer!=null)
                    StageManager.AccountStage(primaryStage); // AccountStage - main stage of this app
                else
                    StageManager.LoginStage();
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
