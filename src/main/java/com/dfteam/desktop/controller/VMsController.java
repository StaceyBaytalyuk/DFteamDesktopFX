package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.VM;
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
import javafx.scene.shape.Circle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;

/**
 * Class controller of vmTableStage
 */
public class VMsController {

    private long createVMClickTime;

    public static String getType() {
        return type;
    }

    public static String getAccName() {
        return accName;
    }

    private static String type;
    private static String accName;

    public static void setType(String type2) {
        type = type2;
    }

    public static void setAccName(String accName2) {
        accName = accName2;
    }

    private ObservableList<VM> VMsList = FXCollections.observableArrayList();

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, Circle> status;

    @FXML
    private TableColumn<VM, String> name;

    @FXML
    private TableColumn<VM, String> ip;

    @FXML
    private TableColumn<VM, String> zone;

    @FXML
    private TableColumn<VM, Button> info;

    @FXML
    private Button refreshBtn;

    @FXML
    public Button createVMbtn;

    @FXML
    private void initialize() {
        if(TokenChecker.isValid()) {
            initData();
            status.setCellValueFactory(new PropertyValueFactory<VM, Circle>("status_circle"));
            name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
            ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
            zone.setCellValueFactory(new PropertyValueFactory<VM, String>("zone"));
            info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));
            table.setItems(VMsList);

            refreshBtn.setOnAction(event -> initData());
            createVMbtn.setOnAction(event -> {
                if (createVMClickTime == 0 || (System.currentTimeMillis() - createVMClickTime > 3000)) {
                    createVMClickTime = System.currentTimeMillis();
                    try {
                        StageManager.CreateVMStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StageManager.hideVMTable();
        }
    }

    /**
     * Get data for table
     */
    private void initData() {
        VMsList.clear();
        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(Request.get("http://34.202.9.91:8000/"+type+"/"+accName+"/allvms"));
            //Check VMs count
            if(json.size()==0)
                TrayNotification.showNotification("VMs not found!");
            else
                for (int i = 0; i < json.size(); i++)
                    VMsList.add(new VM((JSONObject) json.get(i), type, accName));

        } catch (Exception e) {
            TrayNotification.showNotification("VMs not found!");
        }
    }
}
