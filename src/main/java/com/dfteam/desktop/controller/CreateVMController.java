package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.account.Account;
import com.dfteam.apisdk.util.createvm.OS;
import com.dfteam.apisdk.util.createvm.Region;
import com.dfteam.apisdk.util.createvm.Type;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.List;

/**
 * Class controller of createVMStage
 */
public class CreateVMController {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Region> regionSelect;

    @FXML
    private ComboBox<Type> typeSelect;

    @FXML
    private ComboBox<OS> osSelect;

    @FXML
    private Button createBtn;

    private Account account;

    private long createClickTime = 0;

    @FXML
    private void initialize() {
        account = new Account(VMsController.getAccName(), VMsController.getType());
        List<Region> region = account.getRegionList();

        regionSelect.getItems().addAll(region);

        regionSelect.setOnAction(event -> CreateVMController.this.getType());

        regionSelect.setPromptText("No selection");
        regionSelect.setEditable(false); // user can choose, but not type in

        typeSelect.setPromptText("No selection");
        typeSelect.setEditable(false);

        osSelect.setPromptText("No selection");
        osSelect.setEditable(false);

        getOS();

        createBtn.setOnAction(event -> {
            if (createClickTime == 0 || (System.currentTimeMillis() - createClickTime) > Main.CLICKTIME) {
                createClickTime = System.currentTimeMillis();
                onCreate();
            }
        });
    }

    /**
     * createBtn handler.
     * Check if all the fields are filled.
     * If you fill and then delete - it will also warn you.
     */
    private void onCreate() {
        if (nameField.getText().isEmpty() || regionSelect.getValue() == null ||
                typeSelect.getValue() == null || osSelect.getValue() == null)
            Notification.showWarningNotification("Enter all fields"); // if at least one field isn't chosen

        else if (regionSelect.getValue().toString().isEmpty() ||
                typeSelect.getValue().toString().isEmpty() || osSelect.getValue().toString().isEmpty())
            Notification.showWarningNotification("Enter all fields"); // if user deleted something

        else {
            createBtn.setDisable(true);
            if (account.createVM(nameField.getText(),
                    regionSelect.getSelectionModel().getSelectedItem(),
                    typeSelect.getSelectionModel().getSelectedItem(),
                    osSelect.getSelectionModel().getSelectedItem()) != null)
                Platform.runLater(() -> {
                    Notification.showSuccessNotification("VM is successfully created!");
                    StageManager.hideCreateVM();
                });
            else {
                Platform.runLater(() -> {
                    Notification.showErrorNotification("Can't create VM!");
                    createBtn.setDisable(false);
                });
            }
        }
    }

    /**
     * Get list of VM types available in selected region
     */
    private void getType() {
        if (regionSelect.getSelectionModel().getSelectedIndex() > 0) { // selected region
            Region region = regionSelect.getSelectionModel().getSelectedItem();

            List<Type> type = account.getTypeList(region);
            typeSelect.getItems().clear(); // to avoid copies every time you choose something
            typeSelect.getItems().addAll(type); // add list to menu

        } else {
            typeSelect.getItems().clear();
        }
    }

    /**
     * Get list of OS available in selected region and type
     */
    private void getOS() {
        List<OS> os = account.getOSList();
        osSelect.getItems().clear();
        osSelect.getItems().addAll(os);
    }

}