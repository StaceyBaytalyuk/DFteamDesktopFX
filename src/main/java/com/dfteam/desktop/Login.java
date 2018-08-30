package com.dfteam.desktop;

import com.dfteam.desktop.controller.LoginController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public class Login extends Application {

    public static Stage mainLoginScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainLoginScene = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("DFteam - Login");
        primaryStage.getIcons().add(new Image("/images/DF.png"));
        primaryStage.setScene(new Scene(root));
        if(!new LoginController().validToken())
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
