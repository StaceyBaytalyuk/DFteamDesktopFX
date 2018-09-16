package com.dfteam.desktop.controller;

import com.dfteam.desktop.VM;
import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.TokenChecker;
import com.dfteam.desktop.util.TrayNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;

public class OtherVMController {


    private ObservableList<VM> VMsList = FXCollections.observableArrayList();

    @FXML
    private Button addVMbtn;

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, String> name;

    @FXML
    private TableColumn<VM, String> ip;

    @FXML
    private TableColumn<VM, Button> info;

    // инициализируем форму данными
    @FXML
    private void initialize() {
        if(TokenChecker.isValid()) {
            initData();
            name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
            ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
            info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));

            table.setItems(VMsList);
            addVMbtn.setOnAction(event -> {
                try {
                    StageManager.AddVMsStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            TokenChecker.notValidMessage();
            //TODO
        }
    }

    private void initData() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(Request.get("http://167.99.138.88:8000/oth/allvms"));
            for (int i = 0; i < json.size(); i++) {
                VMsList.add(new VM((JSONObject) json.get(i), "oth", null));
            }
        } catch (Exception e) {
            TrayNotification.showNotification("VMs not found!");
        }
    }
}
