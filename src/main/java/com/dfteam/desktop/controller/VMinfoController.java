package com.dfteam.desktop.controller;

import com.dfteam.apisdk.AWSEC2;
import com.dfteam.apisdk.DigitalOcean;
import com.dfteam.apisdk.GoogleCloud;
import com.dfteam.apisdk.Other;
import com.dfteam.apisdk.exceptions.*;
import com.dfteam.apisdk.util.vm.VMAction;
import com.dfteam.desktop.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Class controller of moreInfoStage
 */
public class VMinfoController {

    private VMAction vmAction;

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

    /**
     * Update More Info window
     */
    private void updateInfo() {
        com.dfteam.apisdk.util.vm.VM vm = null;
        try {
            if ( type.equals("gce") ) vm = GoogleCloud.getVMInfo(accName, id);
            else if ( type.equals("do") ) vm = DigitalOcean.getVMInfo(accName, id);
            else if ( type.equals("ec2") ) vm = AWSEC2.getVMInfo(accName, id);
            else if ( type.equals("oth") ) vm = Other.getVMInfo(id);

            vmAction = vm.getAction();

            System.out.println(vm.getName());
            nameText.setText("Name: " + vm.getName());

            if (vm.isOn() || type.equals("oth")) {
                ipText.setText("IP: " + vm.getIp());
                ipText.setVisible(true);
            } else {
                ipText.setVisible(false);
            }

            if (vm.getAccountType().equals("oth")) statusText.setVisible(false);
            else statusText.setText("Status: " + vm.getStatus());

            if(vm.getAccountType().equals("oth")) OnOffBtn.setVisible(false);
            else {
                if (vm.isOn()) {
                    OnOffBtn.setText("OFF VM");
                    OnOffBtn.setOnAction(event -> {
                        if (ConfirmationDialog.showConfirmation("OFF VM", "Are you sure want to OFF VM?")) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > 2000)) {
                                OnOffClickTime = System.currentTimeMillis();
                                try {
                                    vmAction.TurnOff();
                                    TrayNotification.showNotification("VM is OFF");
                                    updateInfo();
                                } catch (AuthFailException e) {
                                    e.printStackTrace();
                                } catch (ServerNotSetException e) {
                                    System.out.println("Server is not set");
                                    System.exit(1);
                                }
                                catch (InvalidTokenException e) {
                                    StageManager.closeAllWindows();
                                    try {
                                        StageManager.LoginStage();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    Platform.runLater(() ->  StageManager.hideVMTable());
                                }
                                catch (VMErrorException e) {
                                    TrayNotification.showNotification("Error\n" + e.getMessage());
                                }
                            }
                        }
                    });
                } else {
                    OnOffBtn.setText("ON VM");
                    OnOffBtn.setOnAction(event -> {
                        if (ConfirmationDialog.showConfirmation("ON VM", "Are you sure want to ON VM?")) {
                            if (OnOffClickTime == 0 || (System.currentTimeMillis() - OnOffClickTime > 2000)) {
                                OnOffClickTime = System.currentTimeMillis();
                                try {
                                    vmAction.TurnOn();
                                    TrayNotification.showNotification("VM is ON");
                                    updateInfo();
                                } catch (AuthFailException e) {
                                    e.printStackTrace();
                                } catch (ServerNotSetException e) {
                                    System.out.println("Server is not set");
                                    System.exit(1);
                                }
                                catch (InvalidTokenException e) {
                                    StageManager.closeAllWindows();
                                    try {
                                        StageManager.LoginStage();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    Platform.runLater(() ->  StageManager.hideVMTable());
                                }
                                catch (VMErrorException e) {
                                    TrayNotification.showNotification("Error\n" + e.getMessage());
                                }
                            }
                        }
                    });
                }
            }

            deleteBtn.setOnAction(event -> {
                if (ConfirmationDialog.showConfirmation("Delete VM", "Are you sure want to delete VM?")) {
                    if (deleteClickTime == 0 || (System.currentTimeMillis() - deleteClickTime > 2000)) {
                        deleteClickTime = System.currentTimeMillis();
                        try {
                            vmAction.Delete();
                            Platform.runLater(() -> TrayNotification.showNotification("VM is successfully deleted!"));
                            StageManager.hideMoreInfo();
                        } catch (AuthFailException e) {
                            e.printStackTrace();
                        } catch (ServerNotSetException e) {
                            System.out.println("Server is not set");
                            System.exit(1);
                        }
                        catch (InvalidTokenException e) {
                            StageManager.closeAllWindows();
                            try {
                                StageManager.LoginStage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            Platform.runLater(() ->  StageManager.hideVMTable());
                        }
                        catch (VMErrorException e) {
                            TrayNotification.showNotification("Error\n" + e.getMessage());
                        }
                    }
                }
            });

            updateBtn.setOnAction(event -> updateInfo());

            com.dfteam.apisdk.util.vm.VM finalVm = vm;
            loadBtn.setOnAction(event -> {
                if (loadClickTime == 0 || (System.currentTimeMillis() - loadClickTime > 2000)) {
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

        catch (ServerNotSetException e) {
            System.out.println("Server is not set");
            System.exit(1);
        }

        catch (InvalidTokenException e) {
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Platform.runLater(() ->  StageManager.hideVMTable());
        }

        catch (AuthFailException | AccountErrorException | VMErrorException e) {
            TrayNotification.showNotification("Error\n" + e.getMessage());
        }

        catch (ParseException e) {
            e.printStackTrace();
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
