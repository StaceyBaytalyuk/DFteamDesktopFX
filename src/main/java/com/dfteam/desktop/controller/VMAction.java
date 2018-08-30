package com.dfteam.desktop.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class VMAction {

    private String id;
    private String accName;
    private String type;
    private String URL;
    private HttpClient client;

    public VMAction(VM vm){
        this.type = vm.getType();
        this.accName = vm.getAccName();
        this.id = vm.getId();
        this.URL = "http://167.99.138.88:8000/"+this.type+"/"+this.accName+"/vm/"+this.id;
        client = HttpClientBuilder.create().build();
    }

    public void OnVM(){
        HttpPost post = new HttpPost(URL+"/on");
        post.addHeader("Authorization", AccountController.token);
        JSONParser parser = new JSONParser();

        try {
            HttpResponse response = client.execute(post);
            JSONObject json = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
            System.out.println(json.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public void OffVM(){
        HttpPost post = new HttpPost(URL+"/off");
        post.addHeader("Authorization", AccountController.token);
        JSONParser parser = new JSONParser();

        try {
            HttpResponse response = client.execute(post);
            JSONObject json = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
            System.out.println(json.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void DeleteVM(){
        HttpDelete del = new HttpDelete(URL);
        del.addHeader("Authorization", AccountController.token);
        JSONParser parser = new JSONParser();

        try {
            HttpResponse response = client.execute(del);
            JSONObject json = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
            System.out.println(json.toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
