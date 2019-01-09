package com.dfteam.desktop;

import com.dfteam.desktop.controller.VMinfoController;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
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
    private long infoClickTime = 0;

    /**
     * @param vm JSONObject parsed from server response
     * @param type Type of account (example: DO)
     * @param accName Account name (example: dima)
     */
    public VM(JSONObject vm, String type, String accName) {
        try {
            if(vm.get("error")==null) {
                name = (String) vm.get("name");
                this.type = type;

                if (!type.equals("oth")) { // other VMs don't have status ans zone
                    status = (String) vm.get("status");
                    zone = (String) vm.get("zone");
                    if (status.equals("active") || status.equals("running")) {
                        ip = (String) vm.get("ip"); // don't show IP if VM doesn't work
                    }

                    if (type.equals("do")) { // DF way to get ID (only for Digital Ocean)
                        id = new StringBuilder().append(vm.get("id")).toString();
                    } else {                 // normal way to get ID (for others)
                        id = (String) vm.get("id");
                    }

                    this.accName = accName;
                } else {
                    ip = (String) vm.get("ip");
                    id = name;
                }
            }else{
                Notification.showErrorNotification("Error: \n"+vm.get("error"));
            }
        }catch (Exception e){
            Notification.showErrorNotification("Error: \n"+e.getMessage());
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
            Notification.showErrorNotification("Error: \n"+e.getMessage());
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
        Circle c = new Circle();
        c.setRadius(8);
        if ( isOn() ) {
            c.setFill(Color.GREEN);
        } else {
            c.setFill(Color.RED);
        }
        return c;
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
            if (infoClickTime == 0 || (System.currentTimeMillis() - infoClickTime) > Main.CLICKTIME) {
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