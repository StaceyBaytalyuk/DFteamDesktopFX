package com.dfteam.desktop;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;


public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("DFteam - Login");
        primaryStage.getIcons().add(new Image("/images/DF.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
