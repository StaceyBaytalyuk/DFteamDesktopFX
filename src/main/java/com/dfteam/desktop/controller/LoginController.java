package com.dfteam.desktop.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwField;

    @FXML
    private Button btnOK;

    @FXML
    private void initialize() {
        btnOK.setOnAction(event -> onOK());
    }

    protected void onOK() {
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
                Stage accountStage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("accounts.fxml")));
                accountStage.setTitle("DFteam - Accounts");
                accountStage.getIcons().add(new Image("/images/DF.png"));
                accountStage.setScene(new Scene(root));
                accountStage.show();
                System.out.println(json.get("token"));
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Auth Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong login or password");
                alert.showAndWait();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
