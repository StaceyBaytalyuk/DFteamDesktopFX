package com.dfteam.desktop.controller;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    @FXML
    private Button btnOK;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwField;

    @FXML
    private void initialize() {
        btnOK.setOnAction(event -> LoginButtonPress());
    }

    protected void LoginButtonPress() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://167.99.138.88:8000/acc/login");

        List<NameValuePair> arguments = new ArrayList<>(2);
        arguments.add(new BasicNameValuePair("login", loginField.getText()));
        arguments.add(new BasicNameValuePair("password", passwField.getText()));

        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
            if((boolean) json.get("auth") && json.get("error") == null){
//                    JOptionPane.showMessageDialog(null, "Auth success!");
//                Accounts dialog = new Accounts((String) json.get("token"));
//                dialog.pack();
//                dialog.setVisible(true);
//                    setVisible(false);
//                this.dispose();
                System.out.println(json.get("token"));
            }else{
//                JOptionPane.showMessageDialog(null, "Auth Error!!!");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
