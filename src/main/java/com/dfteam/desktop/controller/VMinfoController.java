package com.dfteam.desktop.controller;

import com.dfteam.desktop.VM;
import com.dfteam.desktop.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

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

    private long OnOffClickTime = 0;
    private long deleteClickTime = 0;
    private long loadClickTime = 0;

    private void updateInfo(){
        if(!TokenChecker.isValid()) {
            TokenChecker.notValidMessage();
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
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
                        if ( ConfirmationDialog.showConfirmation("OFF VM", "Are you sure want to OFF VM?") ) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > 2000)) {
                                OnOffClickTime = System.currentTimeMillis();
                                vmAction.OffVM();
                                TrayNotification.showNotification("VM is OFF");
                                updateInfo();
                            }
                        }
                    });
                } else {
                    OnOffBtn.setText("ON VM");
                    OnOffBtn.setOnAction(event->{
                        if ( ConfirmationDialog.showConfirmation("ON VM", "Are you sure want to ON VM?") ) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > 2000)) {
                                OnOffClickTime = System.currentTimeMillis();
                                vmAction.OnVM();
                                TrayNotification.showNotification("VM is ON");
                                updateInfo();
                            }
                        }
                    });
            }

            deleteBtn.setOnAction(event -> {
                if ( ConfirmationDialog.showConfirmation("Delete VM", "Are you sure want to delete VM?") ) {
                    if (deleteClickTime == 0 || (System.currentTimeMillis() - deleteClickTime > 2000)) {
                        deleteClickTime = System.currentTimeMillis();
                        vmAction.DeleteVM();
                        TrayNotification.showNotification("VM is successfully deleted");
                        updateInfo();
                    }
                }
            });

            updateBtn.setOnAction(event->updateInfo());

            loadBtn.setOnAction(event -> {
                if( loadClickTime==0 || (System.currentTimeMillis()-loadClickTime>2000) ){
                    loadClickTime = System.currentTimeMillis();
                    VMLoadController.setIp(vm.getIp());
                    try {
                        StageManager.LoadStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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
