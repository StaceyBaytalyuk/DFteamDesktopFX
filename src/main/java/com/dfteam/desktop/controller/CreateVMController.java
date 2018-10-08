package com.dfteam.desktop.controller;

import com.dfteam.apisdk.AWSEC2;
import com.dfteam.apisdk.DigitalOcean;
import com.dfteam.apisdk.GoogleCloud;
import com.dfteam.apisdk.exceptions.*;
import com.dfteam.apisdk.util.account.Account;
import com.dfteam.apisdk.util.createvm.OS;
import com.dfteam.apisdk.util.createvm.Region;
import com.dfteam.apisdk.util.createvm.Type;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.Notification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
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

    @FXML
    private void initialize() {
        try {
            List<Region> region = null;
            account = new Account(VMsController.getType(), VMsController.getAccName());
            if (account.getType().equals("do")) region = DigitalOcean.getRegionList(account);
            else if (account.getType().equals("gce")) region = GoogleCloud.getRegionList(account);
            else if (account.getType().equals("ec2")) region = AWSEC2.getRegionList(account);

            regionSelect.getItems().addAll(region);

            regionSelect.setOnAction(ev -> CreateVMController.this.getType());

            regionSelect.setPromptText("No selection");
            regionSelect.setEditable(false);

            typeSelect.setPromptText("No selection");
            typeSelect.setEditable(false);

            osSelect.setPromptText("No selection");
            osSelect.setEditable(false);

            CreateVMController.this.getOS();

            createBtn.setOnAction(event -> onCreate());
        }
        catch (ServerNotSetException e) {
            System.out.println("Server is not set");
            System.exit(1);
        }

        catch (InvalidTokenException e) {
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Platform.runLater(() ->  StageManager.hideVMTable());
        }

        catch (Exception e) {
            Notification.showErrorNotification("Error\n" + e.getMessage());
        }
    }

    /**
     * createBtn handler.
     * Check if all the fields are filled.
     * If you fill and then delete - it will also warn you.
     */
    private void onCreate() {
        if (nameField.getText().isEmpty() || regionSelect.getValue() == null ||
                typeSelect.getValue() == null || osSelect.getValue() == null)
            Notification.showWarningNotification("Enter all fields");

        else if (regionSelect.getValue().toString().isEmpty() ||
                typeSelect.getValue().toString().isEmpty() || osSelect.getValue().toString().isEmpty())
            Notification.showWarningNotification("Enter all fields");

        else {
            createBtn.setDisable(true);
            try {
                if (account.getType().equals("do"))
                    DigitalOcean.createVM(
                            account,
                            nameField.getText(),
                            regionSelect.getSelectionModel().getSelectedItem(),
                            typeSelect.getSelectionModel().getSelectedItem(),
                            osSelect.getSelectionModel().getSelectedItem());
                else if (account.getType().equals("gce"))
                    GoogleCloud.createVM(
                            account,
                            nameField.getText(),
                            regionSelect.getSelectionModel().getSelectedItem(),
                            typeSelect.getSelectionModel().getSelectedItem(),
                            osSelect.getSelectionModel().getSelectedItem());
                else if (account.getType().equals("ec2"))
                    AWSEC2.createVM(
                            account,
                            nameField.getText(),
                            regionSelect.getSelectionModel().getSelectedItem(),
                            typeSelect.getSelectionModel().getSelectedItem(),
                            osSelect.getSelectionModel().getSelectedItem());
                Platform.runLater(() -> {
                    Notification.showSuccessNotification("VM is successfully created!");
                    StageManager.hideCreateVM();
                });
            }

            catch (ServerNotSetException e) {
                System.out.println("Server is not set");
                System.exit(1);
            }

            catch (InvalidTokenException e) {
                StageManager.closeAllWindows();
                try {
                    StageManager.LoginStage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Platform.runLater(() ->  StageManager.hideVMTable());
            }

            catch (VMErrorException e) {
                Notification.showErrorNotification("Error\nCan't create VM!\n" + e.getMessage());
                createBtn.setDisable(false);
            }

            catch (Exception e) {
                Notification.showErrorNotification("Error\n" + e.getMessage());
                createBtn.setDisable(false);
            }
        }
    }

    /**
     * Get list of VM types available in selected region
     */
    private void getType(){
        if(regionSelect.getSelectionModel().getSelectedIndex()>0) {
            try {
                Region region = regionSelect.getSelectionModel().getSelectedItem();

                List<Type> type = null;

                if (account.getType().equals("do")) type = DigitalOcean.getTypeList(account, region);
                else if (account.getType().equals("gce")) type = GoogleCloud.getTypeList(account, region);
                else if (account.getType().equals("ec2")) type = AWSEC2.getTypeList(account, region);

                typeSelect.getItems().clear();

                typeSelect.getItems().addAll(type);

                typeSelect.setOnAction(ev -> CreateVMController.this.getOS());
            }

            catch (ServerNotSetException e) {
                System.out.println("Server is not set");
                System.exit(1);
            }

            catch (InvalidTokenException e) {
                StageManager.closeAllWindows();
                try {
                    StageManager.LoginStage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Platform.runLater(() ->  StageManager.hideVMTable());
            }

            catch (Exception e) {
                Notification.showErrorNotification("Error\n" + e.getMessage());
            }
        }else{
            typeSelect.getItems().clear();
        }
    }

    /**
     * Get list of OS available in selected region and type
     */
    private void getOS() {
        try {

            List<OS> os = null;

            if (account.getType().equals("do"))
                os = DigitalOcean.getOSList(account);
            else if (account.getType().equals("gce"))
                os = GoogleCloud.getOSList(account);
            else if (account.getType().equals("ec2"))
                os = AWSEC2.getOSList(account);

            osSelect.getItems().clear();

            osSelect.getItems().addAll(os);
        }
        catch (ServerNotSetException e) {
            System.out.println("Server is not set");
            System.exit(1);
        }
        catch (InvalidTokenException e) {
            StageManager.closeAllWindows();
            try {
                StageManager.LoginStage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Platform.runLater(() -> StageManager.hideVMTable());
        }  catch (Exception e) {
            Notification.showErrorNotification("Error\n" + e.getMessage());
        }
    }

}
