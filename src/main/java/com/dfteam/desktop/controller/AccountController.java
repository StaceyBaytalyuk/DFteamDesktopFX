package com.dfteam.desktop.controller;

import com.dfteam.desktop.Request;
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

public class AccountController {
    public static String token;

    @FXML
    private VBox googPanel;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox oceanPanel;

    @FXML
    private VBox amazPanel;

    @FXML
    private void initialize() {
        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        try {
            FileReader tmp = new FileReader(file2);
            JSONObject json = (JSONObject) parser.parse(tmp);
            tmp.close();
            token = (String) json.get("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if ( LoginController.validToken() ) {
                JSONObject json = (JSONObject) parser.parse(Request.get("http://167.99.138.88:8000/accountlist"));
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
                scroll.setFitToHeight(true);
            } else LoginController.notValidMessage();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void openVMs(String type, String accName) {
        VMsController.setType(type);
        VMsController.setAccName(accName);
        VMsController vMsController = new VMsController();

        try {
            vMsController.VMsTableWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
