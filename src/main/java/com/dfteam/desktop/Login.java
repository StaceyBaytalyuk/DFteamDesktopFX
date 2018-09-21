package com.dfteam.desktop;

import com.dfteam.desktop.util.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        StageManager.LoginStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
