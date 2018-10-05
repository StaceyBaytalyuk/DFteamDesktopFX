package com.dfteam.desktop.util;

import com.dfteam.desktop.VM;
import com.dfteam.desktop.controller.VMinfoController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class that allow to on/off, delete and get full info about particular VM
 */
public class VMAction {

    private JSONParser parser = new JSONParser();
    private String accName;
    private String type;
    private String URL;

    public VMAction(){
        this.type = VMinfoController.getType();
        this.accName = VMinfoController.getAccName();
        if (type.equals("oth")) this.URL = "http://34.202.9.91:8000/"+this.type+"/vm/"+VMinfoController.getId();
        else this.URL = "http://34.202.9.91:8000/"+this.type+"/"+this.accName+"/vm/"+VMinfoController.getId();
    }
    /**
     * Get full info about particular VM
     */
    public VM getAllInfo(){
        try {
            System.out.println(URL);
            JSONObject json = (JSONObject) parser.parse(Request.get(URL));
            System.out.println(json.toString());
            return new VM(json, type, accName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void OnVM(){
        Request.post(URL+"/on", null);
    }

    public void OffVM(){
        Request.post(URL+"/off", null);
    }

    public void DeleteVM(){
        Request.delete(URL);
    }
}
