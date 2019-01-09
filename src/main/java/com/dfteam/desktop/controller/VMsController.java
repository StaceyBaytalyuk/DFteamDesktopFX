package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.account.Account;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.VM;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.util.List;

/**
 * Class controller of vmTableStage
 */
public class VMsController {

    private long createVMClickTime = 0;

    private static String type;
    private static String accName;
    public static String getType() {
        return type;
    }
    public static String getAccName() {
        return accName;
    }
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
        initData();
        status.setCellValueFactory(new PropertyValueFactory<VM, Circle>("status_circle"));
        name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
        ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
        zone.setCellValueFactory(new PropertyValueFactory<VM, String>("zone"));
        info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));
        table.setItems(VMsList);

        refreshBtn.setOnAction(event -> initData());
        createVMbtn.setOnAction(event -> {
            if (createVMClickTime == 0 || (System.currentTimeMillis() - createVMClickTime > Main.CLICKTIME)) {
                createVMClickTime = System.currentTimeMillis();
                try {
                    StageManager.CreateVMStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get data for table
     */
    private void initData() {
        VMsList.clear();
        List<com.dfteam.apisdk.util.vm.VM> vm = new Account(accName, type).getVMList();
            if (vm.size() > 0) {
                for (int i = 0; i < vm.size(); i++) {
                    VMsList.add(new VM(vm.get(i)));
                }
            } else {
                Notification.showWarningNotification("VMs not found!");
            }
    }
}