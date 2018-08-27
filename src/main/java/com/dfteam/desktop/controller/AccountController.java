package com.dfteam.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AccountController {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    @FXML
    private VBox googPanel;

    @FXML
    private VBox oceanPanel;

    @FXML
    private VBox amazPanel;

    public AccountController(){

    }

    @FXML
    private void initialize() {
        File file2 = new File(System.getProperty("user.home")+File.separator+".dfteam"+File.separator+"config.json");
        JSONParser parser = new JSONParser();
        try {
            FileReader tmp = new FileReader(file2);
            JSONObject json = (JSONObject) parser.parse(tmp);
            tmp.close();
            token = (String) json.get("token");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("http://167.99.138.88:8000/accountlist");
        get.addHeader("Authorization", token);

        try {
            HttpResponse response = client.execute(get);
            JSONObject json = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
            System.out.println(json.toString());
            JSONArray arr = (JSONArray)json.get("do");
            for(int i =0; i<arr.size(); i++){
                System.out.println(arr.get(i));
                Button b = new Button((String) arr.get(i));
                oceanPanel.getChildren().add(b);
                /*b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openVMs(token, "do", b.getText());
                    }
                });*/
            }

            arr = (JSONArray)json.get("ec2");
            for(int i =0; i<arr.size(); i++){
                System.out.println(arr.get(i));
                Button b = new Button((String) arr.get(i));
                amazPanel.getChildren().add(b);
                /*b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openVMs(token, "ec2", b.getText());
                    }
                });*/
            }

            arr = (JSONArray)json.get("gce");
            for(int i =0; i<arr.size(); i++){
                System.out.println(arr.get(i));
                Button b = new Button((String) arr.get(i));
                googPanel.getChildren().add(b);
                /*b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openVMs(token, "gce", b.getText());
                    }
                });*/
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    protected void openVMs() {

    }
}
