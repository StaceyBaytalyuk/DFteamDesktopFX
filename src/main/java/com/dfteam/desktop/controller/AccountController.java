package com.dfteam.desktop.controller;

import com.dfteam.apisdk.*;
import com.dfteam.apisdk.exceptions.AccountErrorException;
import com.dfteam.apisdk.exceptions.AuthFailException;
import com.dfteam.apisdk.exceptions.InvalidTokenException;
import com.dfteam.apisdk.exceptions.ServerNotSetException;
import com.dfteam.apisdk.util.account.AccountList;
import com.dfteam.desktop.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class controller of accountStage
 */
public class AccountController {
    public static String token;

    private static File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private static File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");


    @FXML
    private VBox googPanel;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox oceanPanel;

    @FXML
    private VBox amazPanel;

    @FXML
    private Button otherVMsBtn;

    @FXML
    private Button logoutBtn;

    private long othClickTime = 0;

    /**
     * Set content of accountStage
     */
    @FXML
    private void initialize() {
        ApiSDK.setServer("http://34.202.9.91:8000");
        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        if(file2.exists()) {
            try {
                FileReader fileReader = new FileReader(file2);
                JSONObject json = (JSONObject) parser.parse(fileReader);
                fileReader.close();
                token = (String) json.get("token");
                ApiSDK.Auth(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                StageManager.LoginStage();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            StageManager.hideAccounts();
        }

        try {
            AccountList GC = GoogleCloud.getAccountList();
            for (int i = 0; i < GC.size(); i++) {
                Button b = new Button( GC.get(i).getName() );
                googPanel.getChildren().add(b);
                b.setOnAction(event -> openVMs("gce", b.getText()));
            }

            AccountList DO = DigitalOcean.getAccountList();
            for (int i = 0; i < DO.size(); i++) {
                Button b = new Button( DO.get(i).getName() );
                oceanPanel.getChildren().add(b);
                b.setOnAction(event -> openVMs("do", b.getText()));
            }

            AccountList AWS = AWSEC2.getAccountList();
            for (int i = 0; i < AWS.size(); i++) {
                Button b = new Button( AWS.get(i).getName() );
                amazPanel.getChildren().add(b);
                b.setOnAction(event -> openVMs("ec2", b.getText()));
            }

            otherVMsBtn.setOnAction(event -> {
                if (othClickTime == 0 || (System.currentTimeMillis() - othClickTime > 2000)) {
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
                    if(HomeDir.exists()){
                        ConfigFile.delete();
                    }
                    StageManager.closeAllWindows();
                    try {
                        StageManager.LoginStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StageManager.hideAccounts();
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
            Platform.runLater(() ->  StageManager.hideVMTable());
        }

        catch (AuthFailException | AccountErrorException e) {
            TrayNotification.showNotification("Error\n" + e.getMessage());
        }

        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open table with VMs when you press a button with account name
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
