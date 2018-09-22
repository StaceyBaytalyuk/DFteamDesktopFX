package com.dfteam.desktop;

import com.dfteam.desktop.controller.VMinfoController;
import com.dfteam.desktop.util.StageManager;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.json.simple.JSONObject;
import java.io.IOException;

public class VM {

    private String id, name, type, accName="", ip="", zone="", status="";

    public VM(JSONObject vm, String type, String accName) {
        name=(String) vm.get("name");
        this.type=type;

        if( !type.equals("oth") ) {
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

    public Button getInfo() {
        Button b = new Button("More info");
        b.setOnAction(event -> OpenInfoWindow());
        return b;
    }

    private void OpenInfoWindow(){
        VMinfoController.setType(type);
        VMinfoController.setAccName(accName);
        VMinfoController.setId(id);
        try {
            StageManager.MoreInfoStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
