package com.dfteam.desktop.controller;

import com.dfteam.desktop.util.Request;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
        String regionResponse = Request.get("http://167.99.138.88:8000/"
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getType(){
        String region = regionSelect.getSelectionModel().getSelectedItem().toString();
        String typeResponse = Request.get("http://167.99.138.88:8000/"
                +VMsController.getType()+"/"+VMsController.getAccName()+"/region/"
                +region+"/gettypes" );
        System.out.println(typeResponse);

        try {
            JSONObject tmp = (JSONObject) parser.parse(typeResponse);
            JSONArray typeArr = (JSONArray) tmp.get("type");
            for (int i=0; i<typeArr.size(); i++) {
                tmp = (JSONObject)typeArr.get(i);
                typeSelect.getItems().add(tmp.get("name"));
            }
            typeSelect.setOnAction((Event ev) ->getOS());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getOS() {
        String region = regionSelect.getSelectionModel().getSelectedItem().toString();
        String request;
        if(VMsController.getType().equals("ec2"))
            request = "http://167.99.138.88:8000/"
                    +VMsController.getType()+"/"
                    +VMsController.getAccName()
                    +"/allimages/"
                    +region;
        else
            request = "http://167.99.138.88:8000/"
                    +VMsController.getType()+"/"
                    +VMsController.getAccName()
                    +"/allimages";
        String typeResponse = Request.get(request);
        System.out.println(typeResponse);

        try {
            JSONObject tmp;
            JSONArray typeArr = (JSONArray) parser.parse(typeResponse);
            for (int i=0; i<typeArr.size(); i++) {
                tmp = (JSONObject)typeArr.get(i);
                osSelect.getItems().add(tmp.get("name"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
