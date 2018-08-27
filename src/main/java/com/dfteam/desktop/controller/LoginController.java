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

import java.io.*;
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
        if(HomeDir.exists()){
            if(validToken()){
                try {
                    StartAccount();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            HomeDir.mkdir();
        }
        btnOK.setOnAction(event -> onOK());
    }

    private File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");

    private boolean validToken(){
        JSONObject json;
        JSONParser parser = new JSONParser();
        if(ConfigFile.exists()){
            try {
                FileReader tmp = new FileReader(ConfigFile);
                json = (JSONObject) parser.parse(tmp);
                tmp.close();
                return json.get("token") != null;
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void AddFile(String token){
        JSONObject json = new JSONObject();
        try {
            ConfigFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        json.put("token", token);
        try {
            FileWriter tmp2 = new FileWriter(ConfigFile);
            tmp2.write(json.toJSONString());
            tmp2.flush();
            tmp2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                AddFile((String) json.get("token"));
                System.out.println(json.get("token"));
                StartAccount();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Auth Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong login or password");
                alert.showAndWait();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void StartAccount() throws IOException {

        Stage accountStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("accounts.fxml")));
        accountStage.setTitle("DFteam - Accounts");
        accountStage.getIcons().add(new Image("/images/DF.png"));
        accountStage.setScene(new Scene(root));
        accountStage.show();
    }
}
