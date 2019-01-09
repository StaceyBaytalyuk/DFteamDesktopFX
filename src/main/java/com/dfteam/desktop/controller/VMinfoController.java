package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.account.Account;
import com.dfteam.apisdk.util.vm.VM;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.ConfirmationDialog;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.io.IOException;

/**
 * Class controller of moreInfoStage
 */
public class VMinfoController {

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
        updateInfo();
    }

    private long OnOffClickTime = 0;
    private long deleteClickTime = 0;
    private long loadClickTime = 0;

    private VM vm;

    /**
     * Update More Info window
     */
    private void updateInfo() {
        try {
            this.vm = new Account(accName, type).getVMInfo(id);

            nameText.setText("Name: " + vm.getName());

            if (vm.isOn() || type.equals("oth")) { // show IP only if VM works or it is Other VM
                ipText.setText("IP: " + vm.getIp());
                ipText.setVisible(true);
            } else {
                ipText.setVisible(false);
            }

            if (vm.getAccountType().equals("oth")) statusText.setVisible(false);
            else statusText.setText("Status: " + vm.getStatus()); // Other VM don't have status

            if (vm.getAccountType().equals("oth")) OnOffBtn.setVisible(false);
            else { // can't On/Off Other VM
                if (vm.isOn()) {
                    OnOffBtn.setText("OFF VM");
                    OnOffBtn.setOnAction(event -> {
                        if (ConfirmationDialog.showConfirmation("OFF VM", "Are you sure want to OFF VM?")) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > Main.CLICKTIME)) {
                                OnOffClickTime = System.currentTimeMillis();
                                if (vm.TurnOff()) {
                                    Notification.showSuccessNotification("VM is OFF");
                                    updateInfo();
                                } else
                                    Notification.showWarningNotification("Can't OFF VM!");
                            }
                        }
                    });
                } else {
                    OnOffBtn.setText("ON VM");
                    OnOffBtn.setOnAction(event -> {
                        if (ConfirmationDialog.showConfirmation("ON VM", "Are you sure want to ON VM?")) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > Main.CLICKTIME)) {
                                OnOffClickTime = System.currentTimeMillis();
                                if (vm.TurnOn()) {
                                    Notification.showSuccessNotification("VM is ON");
                                    updateInfo();
                                } else
                                    Notification.showWarningNotification("Can't ON VM!");
                            }
                        }
                    });
                }
            }

            deleteBtn.setOnAction(event -> {
                if (ConfirmationDialog.showConfirmation("Delete VM", "Are you sure want to delete VM?")) {
                    if (deleteClickTime == 0 || (System.currentTimeMillis() - deleteClickTime > Main.CLICKTIME)) {
                        deleteClickTime = System.currentTimeMillis();
                        if (vm.Delete() != null) {
                            Notification.showSuccessNotification("VM is successfully deleted!");
                            StageManager.hideMoreInfo();
                        } else
                            Notification.showWarningNotification("Can't delete VM!");
                    }
                }
            });

            updateBtn.setOnAction(event -> updateInfo());

            com.dfteam.apisdk.util.vm.VM finalVm = vm;
            loadBtn.setOnAction(event -> {
                if (loadClickTime == 0 || (System.currentTimeMillis() - loadClickTime > Main.CLICKTIME)) {
                    loadClickTime = System.currentTimeMillis();
                    VMLoadController.setIp(finalVm.getIp());
                    try {
                        StageManager.LoadStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param type2 type of account (example: DO)
     */
    public static void setType(String type2){
        type = type2;
    }

    /**
     * @param accName2 account name (example: dima)
     */
    public static void setAccName(String accName2){
        accName = accName2;
    }

    /**
     * @param id2 VM's id
     */
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