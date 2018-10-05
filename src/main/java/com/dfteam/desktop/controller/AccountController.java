package com.dfteam.desktop.controller;

import com.dfteam.apisdk.ApiSDK;
import com.dfteam.apisdk.GoogleCloud;
import com.dfteam.apisdk.Other;
import com.dfteam.apisdk.util.vm.VM;
import com.dfteam.desktop.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
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
        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        if(file2.exists()) {
            try {
                FileReader fileReader = new FileReader(file2);
                JSONObject json = (JSONObject) parser.parse(fileReader);
                fileReader.close();
                token = (String) json.get("token");
//                ApiSDK.setServer("http://34.202.9.91:8000");
//                ApiSDK.Auth(token);
//                for (VM select : Other.getVMList())
//                    System.out.println(select);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            StageManager.hideAccounts();
        }

        try {
            if ( TokenChecker.isValid() ) {
                JSONObject json = (JSONObject) parser.parse(Request.get("http://34.202.9.91:8000/accountlist"));
                JSONArray arr = (JSONArray) json.get("do");
                for (int i = 0; i < arr.size(); i++) {
                    Button b = new Button((String) arr.get(i));
                    oceanPanel.getChildren().add(b);
                    b.setOnAction(event -> openVMs("do", b.getText()));
                }

                arr = (JSONArray) json.get("ec2");
                for (int i = 0; i < arr.size(); i++) {
                    Button b = new Button((String) arr.get(i));
                    amazPanel.getChildren().add(b);
                    b.setOnAction(event -> openVMs("ec2", b.getText()));
                }

                arr = (JSONArray) json.get("gce");
                for (int i = 0; i < arr.size(); i++) {
                    Button b = new Button((String) arr.get(i));
                    googPanel.getChildren().add(b);
                    b.setOnAction(event -> openVMs("gce", b.getText()));
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
            } else {
                StageManager.closeAllWindows();
                try {
                    StageManager.LoginStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StageManager.hideAccounts();
            }

        } catch (ParseException e) {
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
