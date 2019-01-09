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
import java.io.IOException;
import java.util.List;

/**
 * Class controller of otherVMStage
 */
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

    @FXML
    private Button refreshBtn;

    private long addVMClickTime = 0;

    @FXML
    private void initialize() {
        initData();
        // fill table
        name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
        ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
        info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));
        table.setItems(VMsList);

        refreshBtn.setOnAction(event -> {
            initData();
        });

        addVMbtn.setOnAction(event -> {
            if (addVMClickTime == 0 || (System.currentTimeMillis() - addVMClickTime > Main.CLICKTIME)) {
                addVMClickTime = System.currentTimeMillis();
                try {
                    StageManager.AddVMStage();
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
        VMsList.clear(); // to avoid copies
        List<com.dfteam.apisdk.util.vm.VM> vm = new Account(" ", "oth").getVMList();
        if (vm.size() > 0) {
            for (com.dfteam.apisdk.util.vm.VM vm1 : vm) {
                VMsList.add(new VM(vm1));
            }
        } else {
            Notification.showWarningNotification("VMs not found!");
        }
    }
}