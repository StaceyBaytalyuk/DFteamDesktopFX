package com.dfteam.desktop.controller;

import com.dfteam.apisdk.*;
import com.dfteam.apisdk.exceptions.AccountErrorException;
import com.dfteam.apisdk.exceptions.AuthFailException;
import com.dfteam.apisdk.exceptions.InvalidTokenException;
import com.dfteam.apisdk.exceptions.ServerNotSetException;
import com.dfteam.apisdk.util.account.AccountList;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.IOException;

/**
 * Class controller of accountStage
 */
public class AccountController {
    public static String token;

    private static File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private static File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");

    @FXML
    private ScrollPane scroll;

    // columns which will contain buttons with account names
    @FXML
    private VBox googPanel;

    @FXML
    private VBox oceanPanel;

    @FXML
    private VBox amazPanel;

    // buttons in the bottom of the window
    @FXML
    private Button otherVMsBtn;

    @FXML
    private Button logoutBtn;

    private long othClickTime = 0;
    private long gceClickTime = 0;
    private long doClickTime = 0;
    private long ec2ClickTime = 0;

    /**
     * Set content of accountStage
     */
    @FXML
    private void initialize() {
        try {
            AccountList GC = GoogleCloud.getAccountList();
            for (int i = 0; i < GC.size(); i++) {
                Button b = new Button( GC.get(i).getName() );
                googPanel.getChildren().add(b); // add button to the column
                b.setOnAction(event -> { // open VM Table when you press the button
                    if (gceClickTime == 0 || (System.currentTimeMillis() - gceClickTime) > Main.CLICKTIME) {
                        gceClickTime = System.currentTimeMillis();
                        openVMs("gce", b.getText());
                    }
                });
            }

            AccountList DO = DigitalOcean.getAccountList();
            for (int i = 0; i < DO.size(); i++) {
                Button b = new Button( DO.get(i).getName() );
                oceanPanel.getChildren().add(b);
                b.setOnAction(event -> {
                    if (doClickTime == 0 || (System.currentTimeMillis() - doClickTime) > Main.CLICKTIME) {
                        doClickTime = System.currentTimeMillis();
                        openVMs("do", b.getText());
                    }

                });
            }

            AccountList AWS = AWSEC2.getAccountList();
            for (int i = 0; i < AWS.size(); i++) {
                Button b = new Button( AWS.get(i).getName() );
                amazPanel.getChildren().add(b);
                b.setOnAction(event -> {
                    if (ec2ClickTime == 0 || (System.currentTimeMillis() - ec2ClickTime) > Main.CLICKTIME) {
                        ec2ClickTime = System.currentTimeMillis();
                        openVMs("ec2", b.getText());
                    }

                });
            }

            otherVMsBtn.setOnAction(event -> {
                if (othClickTime == 0 || (System.currentTimeMillis() - othClickTime > Main.CLICKTIME)) {
                    othClickTime = System.currentTimeMillis();
                    try {
                        StageManager.OtherVMsStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            logoutBtn.setOnAction(event -> {
                if (ConfirmationDialog.showConfirmation("Log Out", "Are you sure want to log out?")) {
                    if(HomeDir.exists()){ // delete file with token
                        ConfigFile.delete();
                    }
                    StageManager.closeAllWindows();
                    try {
                        StageManager.LoginStage(); // let user to log in again
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StageManager.hideAccounts(); // current window should be closed after everything is done
                }
            });

            scroll.setFitToHeight(true);
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
            Platform.runLater(() ->  StageManager.hideVMTable()); // multithreading in GUI make lots of problems, so I need to use this thing
        }

        catch (AuthFailException | AccountErrorException e) {
            Notification.showErrorNotification("Error\n" + e.getMessage());
        }

        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open table with VMs when you press a button with account name
     * @param type type of account (example: DO)
     * @param accName account name (example: dima)
     */
    private void openVMs(String type, String accName) {
        VMsController.setType(type);
        VMsController.setAccName(accName);
        try {
            StageManager.VMTableStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
