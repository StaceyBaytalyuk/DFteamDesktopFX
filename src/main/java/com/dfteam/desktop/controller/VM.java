package com.dfteam.desktop.controller;

import org.json.simple.JSONObject;

public class VM {

    private String id, name, type, accName, ip="", zone, status;

    public VM(JSONObject vm, String type, String accName) {
        id=(String) vm.get("id");
        name=(String) vm.get("name");
        status=(String) vm.get("status");
        zone=(String) vm.get("zone");
        if(status.equals("active") || status.equals("running")) {
            ip=(String) vm.get("ip");
        }
        this.type=type;
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
}
