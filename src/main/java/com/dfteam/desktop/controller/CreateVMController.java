package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.Request;
import com.dfteam.desktop.util.StageManager;
import com.dfteam.desktop.util.TrayNotification;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class controller of createVMStage
 */
public class CreateVMController {

    private JSONParser parser = new JSONParser();

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox regionSelect;

    @FXML
    private ComboBox typeSelect;

    @FXML
    private ComboBox osSelect;

    @FXML
    private Button createBtn;

    @FXML
    private void initialize() {
        String regionResponse = Request.get("http://34.202.9.91:8000/"
                +VMsController.getType()+"/"+VMsController.getAccName()+"/allregion" );
        System.out.println(regionResponse);

        try {
            JSONArray regionArr = (JSONArray) parser.parse(regionResponse);
            JSONObject jsonObj;
            for (int i=0; i<regionArr.size(); i++) {
                jsonObj = (JSONObject)regionArr.get(i);
                regionSelect.getItems().add(jsonObj.get("name"));
            }

            regionSelect.setOnAction((Event ev) ->getType());

            regionSelect.setPromptText("No selection");
            regionSelect.setEditable(true);

            typeSelect.setPromptText("No selection");
            typeSelect.setEditable(true);

            osSelect.setPromptText("No selection");
            osSelect.setEditable(true);

            createBtn.setOnAction(event -> {
//                if ( nameField.getText().isEmpty() && )
                System.out.println(nameField.getText().isEmpty());
                //nameField.getText().isEmpty();
                Map<String, String> hashMap = new HashMap<String, String>();
                String request = "http://34.202.9.91:8000/";
                request += VMsController.getType() + "/" + VMsController.getAccName() + "/vm/create";

                    hashMap.put("name", nameField.getText());
                    hashMap.put("region", regionSelect.getValue().toString());
                    hashMap.put("type", typeSelect.getValue().toString());
                    hashMap.put("os", osSelect.getValue().toString());
                System.out.println(hashMap.toString());
                JSONObject response = Request.post(request, hashMap);
                System.out.println(response.toString());
                if ( response.size()>2){
                    TrayNotification.showNotification("VM is successfully created!");
                    StageManager.hideCreateVM();
                } else {
                    TrayNotification.showNotification("Error\nCan't create VM!\n"+response.get("error"));
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get list of VM types available in selected region
     */
    private void getType(){
        String region = regionSelect.getSelectionModel().getSelectedItem().toString();
        String typeResponse = Request.get("http://34.202.9.91:8000/"
                +VMsController.getType()+"/"+VMsController.getAccName()+"/region/"
                +region+"/gettypes" );
        System.out.println(typeResponse);

        try {
            JSONObject tmp = (JSONObject) parser.parse(typeResponse);
            JSONArray typeArr = (JSONArray) tmp.get("type");
            typeSelect.getItems().clear();
            for (int i=0; i<typeArr.size(); i++) {
                tmp = (JSONObject)typeArr.get(i);
                typeSelect.getItems().add(tmp.get("name"));
            }
            typeSelect.setOnAction((Event ev) ->getOS());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get list of OS available in selected region and type
     */
    private void getOS() {
        String region = regionSelect.getSelectionModel().getSelectedItem().toString();
        String request;
        if(VMsController.getType().equals("ec2"))
            request = "http://34.202.9.91:8000/"
                    +VMsController.getType()+"/"
                    +VMsController.getAccName()
                    +"/allimages/"
                    +region;
        else
            request = "http://34.202.9.91:8000/"
                    +VMsController.getType()+"/"
                    +VMsController.getAccName()
                    +"/allimages";
        String typeResponse = Request.get(request);
        System.out.println(typeResponse);

        try {
            JSONObject tmp;
            JSONArray typeArr = (JSONArray) parser.parse(typeResponse);
            osSelect.getItems().clear();
            for (int i=0; i<typeArr.size(); i++) {
                tmp = (JSONObject)typeArr.get(i);
                osSelect.getItems().add(tmp.get("name"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
