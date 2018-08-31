package com.dfteam.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
import java.util.Objects;

public class VMinfoController {

    private VMAction vmAction = new VMAction();
    private VM vm = vmAction.getAllInfo();

    private static String type;

    private static String accName;

    private static String id;

    @FXML
    private Text nameText;

    @FXML
    private Text statusText;

    @FXML
    private Text ipText;

    @FXML
    private Button OnOffBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button loadBtn;

    @FXML
    private Button consoleBtn;

    @FXML
    public void initialize(){
        vmAction = new VMAction();
        updateInfo();
    }

    private void updateInfo(){
        vm = vmAction.getAllInfo();
        nameText.setText("Name: " + vm.getName());

        if(vm.isOn()){
            ipText.setText("IP: " + vm.getIp());
            ipText.setVisible(true);
        } else {
            ipText.setVisible(false);
        }

        statusText.setText("Status: " + vm.getStatus());

        if(vm.isOn()){
            OnOffBtn.setText("OFF VM");
            OnOffBtn.setOnAction(event->{
                vmAction.OffVM();
                updateInfo();
            });

        } else {
            OnOffBtn.setText("ON VM");
            OnOffBtn.setOnAction(event->{
                vmAction.OnVM();
                updateInfo();
            });
        }

        deleteBtn.setOnAction(event -> {
            vmAction.DeleteVM();
            updateInfo();
        });

//        consoleBtn.setOnAction(event->updateInfo());
    }

    public void show() throws IOException {
        Stage accountStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("vminfo.fxml")));
        accountStage.setTitle("DFteam - VMs");
        accountStage.getIcons().add(new Image("/images/DF.png"));
        accountStage.setScene(new Scene(root));
        accountStage.show();
    }

    public static void setType(String type2){
        type = type2;
    }

    public static void setAccName(String accName2){
        accName = accName2;
    }

    public static void setId(String id2){
        id = id2;
    }

    public static String getType() {
        return type;
    }

    public static String getAccName() {
        return accName;
    }

    public static String getId() {
        return id;
    }
}
