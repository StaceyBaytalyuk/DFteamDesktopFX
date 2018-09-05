package com.dfteam.desktop.controller;

import com.dfteam.desktop.VM;
import com.dfteam.desktop.VMAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class VMinfoController {

    private VMAction vmAction = new VMAction();
    private VM vm = vmAction.getAllInfo();

    private static String type;

    private static String accName;

    private static String id;

    @FXML
    private Text nameText;

    @FXML
    private Text statusText;

    @FXML
    private Text ipText;

    @FXML
    private Button OnOffBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button loadBtn;

    @FXML
    private Button consoleBtn;

    @FXML
    private Button updateBtn;

    @FXML
    public void initialize(){
        vmAction = new VMAction();
        updateInfo();
    }

    private void updateInfo(){
        vm = vmAction.getAllInfo();
        nameText.setText("Name: " + vm.getName());

        if(vm.isOn()){
            ipText.setText("IP: " + vm.getIp());
            ipText.setVisible(true);
        } else {
            ipText.setVisible(false);
        }

        statusText.setText("Status: " + vm.getStatus());

        if(vm.isOn()){
            OnOffBtn.setText("OFF VM");
            OnOffBtn.setOnAction(event->{
                vmAction.OffVM();
                showNotification("VM is OFF");
                updateInfo();
            });
        } else {
            OnOffBtn.setText("ON VM");
            OnOffBtn.setOnAction(event->{
                vmAction.OnVM();
                showNotification("VM is ON");
                updateInfo();
            });
        }

        deleteBtn.setOnAction(event -> {
            vmAction.DeleteVM();
            showNotification("VM is successfully deleted");
            updateInfo();
        });
        
        updateBtn.setOnAction(event->updateInfo());
    }

    private void showNotification(String head) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/DF.png");
            TrayIcon trayIcon = new TrayIcon(image);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(head, "", TrayIcon.MessageType.INFO);
        }
    }

    public void show() throws IOException {
        Stage vmStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("vminfo.fxml")));
        vmStage.setTitle("DFteam - VMs");
        vmStage.getIcons().add(new Image("/images/DF.png"));
        vmStage.setScene(new Scene(root));
        vmStage.show();
    }

    public static void setType(String type2){
        type = type2;
    }

    public static void setAccName(String accName2){
        accName = accName2;
    }

    public static void setId(String id2){
        id = id2;
    }

    public static String getType() {
        return type;
    }

    public static String getAccName() {
        return accName;
    }

    public static String getId() {
        return id;
    }
}
