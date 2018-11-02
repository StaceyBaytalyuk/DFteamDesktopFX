package com.dfteam.desktop.controller;

import com.dfteam.apisdk.Other;
import com.dfteam.apisdk.exceptions.*;
import com.dfteam.apisdk.util.vm.VMList;
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
import org.json.simple.parser.ParseException;

import java.io.IOException;

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
        try {
            initData();
            // fill table
            name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
            ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
            info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));
            table.setItems(VMsList);

            refreshBtn.setOnAction(event -> {
                try {
                    initData();
                } catch (ServerNotSetException e) {
                    System.exit(1);
                }

                catch (InvalidTokenException e) {
                    StageManager.closeAllWindows();
                    try {
                        StageManager.LoginStage();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    StageManager.hideOtherVMs();
                }

                catch (VMErrorException | AccountErrorException e) {
                    Notification.showErrorNotification("Error:\n" + e.getMessage() );
                }

                catch (AuthFailException | ParseException e) {
                    e.printStackTrace();
                }
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
        catch (ServerNotSetException e) {
            System.exit(1);
        }

        catch (InvalidTokenException e) {
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            StageManager.hideOtherVMs();
        }

        catch (VMErrorException | AccountErrorException e) {
            Notification.showErrorNotification("Error:\n" + e.getMessage() );
        }

        catch (AuthFailException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get data for table
     */
    private void initData() throws AuthFailException, VMErrorException, ParseException, InvalidTokenException, AccountErrorException, ServerNotSetException {
        VMsList.clear(); // to avoid copies
        VMList vm = Other.getVMList();
        if(vm.size()>0) {
            for (int i = 0; i < vm.size(); i++) {
                VMsList.add(new VM(vm.get(i)));
            }
        }else{
            Notification.showWarningNotification("VMs not found!");
        }
    }
}
