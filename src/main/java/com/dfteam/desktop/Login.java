package com.dfteam.desktop;

import com.dfteam.desktop.util.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * Main class
 * Try to open accountStage
 */
public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        StageManager.AccountStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
