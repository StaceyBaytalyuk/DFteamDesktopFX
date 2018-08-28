package com.dfteam.desktop.controller;

import javafx.scene.control.Button;
import org.json.simple.JSONObject;

public class VM {

    private String id, name, type, accName, ip="", zone, status;

    public VM(JSONObject vm, String type, String accName) {
        name=(String) vm.get("name");
        status=(String) vm.get("status");
        zone=(String) vm.get("zone");
        if(status.equals("active") || status.equals("running")) {
            ip=(String) vm.get("ip");
        }
        this.type=type;
        if(type.equals("do")) {
            id = new StringBuilder().append(vm.get("id")).toString();
        }else{
            id = (String) vm.get("id");
        }
        this.accName=accName;
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

    public Button getInfo() {
        return new Button("More info");
    }
}
