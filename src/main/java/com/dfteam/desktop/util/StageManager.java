package com.dfteam.desktop.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class StageManager {

    private static Stage loginStage;
    private static Stage accountStage;
    private static Stage vmTableStage;
    private static Stage otherVMsStage;
    private static Stage createVMStage;
    private static Stage addVMStage;
    private static Stage loadStage;
    private static Stage moreInfoStage;

    public static void LoginStage(Stage primaryStage) throws IOException {
        ConnectCheck.check();
        loginStage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("login.fxml")));
        primaryStage.setTitle("DFteam - Login");
        primaryStage.getIcons().add(new Image("/images/DF.png"));
        primaryStage.setScene(new Scene(root));
        if( !TokenChecker.isValid()) {
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

    public static void AccountStage() throws IOException {
        accountStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("accounts.fxml")));
        accountStage.setTitle("DFteam - Accounts");
        accountStage.getIcons().add(new Image("/images/DF.png"));
        accountStage.setScene(new Scene(root));
        accountStage.show();
    }

    public static void VMTableStage() throws IOException {
        vmTableStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("vms.fxml")));
        vmTableStage.setTitle("DFteam - VMs");
        vmTableStage.getIcons().add(new Image("/images/DF.png"));
        vmTableStage.setScene(new Scene(root));
        vmTableStage.show();
    }

    public static void OtherVMsStage() throws IOException {
        otherVMsStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("othervms.fxml")));
        otherVMsStage.setTitle("DFteam - Other VMs");
        otherVMsStage.getIcons().add(new Image("/images/DF.png"));
        otherVMsStage.setScene(new Scene(root));
        otherVMsStage.show();
    }

    public static void CreateVMStage() throws IOException {
        createVMStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("createVM.fxml")));
        createVMStage.setTitle("DFteam - Create VM");
        createVMStage.getIcons().add(new Image("/images/DF.png"));
        createVMStage.setScene(new Scene(root));
        createVMStage.show();
    }

    public static void AddVMStage() throws IOException {
        addVMStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("addVM.fxml")));//TODO addVM.fxml
        addVMStage.setTitle("DFteam - Add VM");
        addVMStage.getIcons().add(new Image("/images/DF.png"));
        addVMStage.setScene(new Scene(root));
        addVMStage.show();
    }

    public static void LoadStage() throws IOException {
        loadStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("VMload.fxml")));
        loadStage.setTitle("DFteam - VM Load");
        loadStage.getIcons().add(new Image("/images/DF.png"));
        loadStage.setScene(new Scene(root));
        loadStage.show();
    }

    public static void MoreInfoStage() throws IOException {
        moreInfoStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(StageManager.class.getClassLoader().getResource("vminfo.fxml")));
        moreInfoStage.setTitle("DFteam - More Info");
        moreInfoStage.getIcons().add(new Image("/images/DF.png"));
        moreInfoStage.setScene(new Scene(root));
        moreInfoStage.show();
    }

    private static void closeWindow(Stage stage){
        if (stage!=null) {
            if(stage.isShowing()){
                stage.hide();
            }
        }
    }

    public static void closeAllWindows(){
        hideAccounts();
        hideVMTable();
        hideOtherVMs();
        hideCreateVM();
        hideAddVM();
        hideLoad();
        hideMoreInfo();
    }

    public static boolean isOpenLoad(){ return /*loadStage.isShowing()*/true; }

    public static void hideLogin(){
        closeWindow(loginStage);
    }

    public static void hideAccounts(){
        closeWindow(accountStage);
    }

    public static void hideVMTable(){
        closeWindow(vmTableStage);
    }

    public static void hideOtherVMs(){
        closeWindow(otherVMsStage);
    }

    public static void hideCreateVM(){
        closeWindow(createVMStage);
    }

    public static void hideAddVM(){
        closeWindow(addVMStage);
    }

    public static void hideLoad(){
        closeWindow(loadStage);
    }

    public static void hideMoreInfo(){
        closeWindow(moreInfoStage);
    }


}
