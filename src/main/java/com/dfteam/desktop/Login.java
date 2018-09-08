package com.dfteam.desktop;

import com.dfteam.desktop.controller.LoginController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Login extends Application {

    public static Stage mainLoginScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        ConnectCheck.check();
        /*Thread connectionThread = new Thread(new Runnable(){
            @Override
            public void run() {
                ConnectCheck.check();
            }
        });
        connectionThread.start();*/
        mainLoginScene = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("DFteam - Login");
        primaryStage.getIcons().add(new Image("/images/DF.png"));
        primaryStage.setScene(new Scene(root));
        if( !LoginController.validToken() ) {
            primaryStage.show();
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/DF.png");
                TrayIcon trayIcon = new TrayIcon(image);
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                trayIcon.displayMessage("Authorization",
                        "Enter your login and password", TrayIcon.MessageType.INFO);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
