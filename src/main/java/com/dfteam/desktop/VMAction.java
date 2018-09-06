package com.dfteam.desktop;

import com.dfteam.desktop.controller.VMinfoController;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class VMAction {


    private JSONParser parser = new JSONParser();
    private String id;
    private String accName;
    private String type;
    private String URL;
    private HttpClient client;

    public VMAction(){
        this.type = VMinfoController.getType();
        this.accName = VMinfoController.getAccName();
        this.id = VMinfoController.getId();
        this.URL = "http://167.99.138.88:8000/"+this.type+"/"+this.accName+"/vm/"+this.id;
        client = HttpClientBuilder.create().build();
    }

    public VM getAllInfo(){

        try {
            JSONObject json = (JSONObject) parser.parse(Request.get(URL));
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
