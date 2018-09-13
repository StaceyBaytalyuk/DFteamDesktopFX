package com.dfteam.desktop.controller;

import com.dfteam.desktop.VM;
import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.util.TokenChecker;
import com.dfteam.desktop.util.TrayNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.util.Objects;

public class OtherVMsController {


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
            addVMbtn.setOnAction(event -> openAddVM());
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
//            e.printStackTrace();
            TrayNotification.showNotification("VMs not found!");
        }
    }

    public void openAddVM()  {
        Stage addVMStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("vms.fxml")));
            addVMStage.setTitle("DFteam - VMs");
            addVMStage.getIcons().add(new Image("/images/DF.png"));
            addVMStage.setScene(new Scene(root));
            addVMStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
