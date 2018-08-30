package com.dfteam.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class VMinfoController {

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


}
