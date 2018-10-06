package com.dfteam.desktop;

import com.dfteam.desktop.controller.VMinfoController;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.TrayNotification;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.json.simple.JSONObject;
import java.io.IOException;

/**
 * Class that contains info about VM
 */
public class VM {

    private String id, name, type, accName="", ip="", zone="", status="";
    private long infoClickTime;

    /**
     * @param vm JSONObject parsed from server response
     * @param type Type of account
     * @param accName Account name
     */

    public VM(JSONObject vm, String type, String accName) {
        try {
            if(vm.get("error")==null) {
                name = (String) vm.get("name");
                this.type = type;

                if (!type.equals("oth")) {
                    status = (String) vm.get("status");
                    zone = (String) vm.get("zone");
                    if (status.equals("active") || status.equals("running")) {
                        ip = (String) vm.get("ip");
                    }

                    if (type.equals("do")) {
                        id = new StringBuilder().append(vm.get("id")).toString();
                    } else {
                        id = (String) vm.get("id");
                    }
                    this.accName = accName;
                } else {
                    ip = (String) vm.get("ip");
                    id = name;
                }
            }else{
                Platform.runLater(() -> TrayNotification.showNotification("Error: \n"+vm.get("error")));
            }
        }catch (Exception e){
            Platform.runLater(() -> TrayNotification.showNotification("Error: \n"+e.getMessage()));
        }
    }

    public VM(com.dfteam.apisdk.util.vm.VM vm) {
        try {
            this.status = vm.getStatus();
            this.name = vm.getName();
            this.id = vm.getId();
            this.zone = vm.getZone();
            this.ip = vm.getIp();
            this.type = vm.getAccountType();
            this.accName = vm.getAccountName();
        } catch (Exception e){
            Platform.runLater(() -> TrayNotification.showNotification("Error: \n"+e.getMessage()));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAccName() {
        return accName;
    }

    public String getIp() {
        return ip;
    }

    public String getZone() {
        return zone;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Displays status of VM like a red or green circle
     * @return Circle for table
     */
    public Circle getStatus_circle(){
        Circle test = new Circle();
        test.setRadius(8);
        if(!isOn())
            test.setFill(Color.RED);
        else
            test.setFill(Color.GREEN);
        return test;
    }

    public boolean isOn(){
        return status.equals("active") || status.equals("running");
    }

    /**
     * Return info about VM
     * @return Button for table
     */
    public Button getInfo() {
        Button b = new Button("More info");
        b.setOnAction(event -> {
            if (infoClickTime == 0 || (System.currentTimeMillis() - infoClickTime) > 2000) {
                infoClickTime = System.currentTimeMillis();
                OpenInfoWindow();
            }
        });
        return b;
    }

    private void OpenInfoWindow(){
        VMinfoController.setType(type);
        VMinfoController.setAccName(accName);
        if(type.equals("oth"))
            VMinfoController.setId(name);
        else
            VMinfoController.setId(id);
        try {
            StageManager.MoreInfoStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
