package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.util.TokenChecker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

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
    private Button otherVMsBtn;

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
            if ( TokenChecker.isValid() ) {
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
                otherVMsBtn.setOnAction(event -> otherVMs());
                scroll.setFitToHeight(true);
            } else {
                TokenChecker.notValidMessage();
                //TODO
            }

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

    private void otherVMs() {
        Stage otherVMsWindow = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("othervms.fxml")));
            otherVMsWindow.setTitle("DFteam - Other VMs");
            otherVMsWindow.getIcons().add(new Image("/images/DF.png"));
            otherVMsWindow.setScene(new Scene(root));
            otherVMsWindow.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
