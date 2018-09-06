package com.dfteam.desktop.controller;

import com.dfteam.desktop.Request;
import com.dfteam.desktop.VM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class VMsController {

    private static String type;
    private static String accName;

    public static void setType(String type2) {
        type = type2;
    }

    public static void setAccName(String accName2) {
        accName = accName2;
    }

    private ObservableList<VM> VMsList = FXCollections.observableArrayList();

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, Circle> status;

    @FXML
    private TableColumn<VM, String> name;

    @FXML
    private TableColumn<VM, String> ip;

    @FXML
    private TableColumn<VM, String> zone;

    @FXML
    private TableColumn<VM, Button> info;

    // инициализируем форму данными
    @FXML
    private void initialize() {
        initData();

        // устанавливаем тип и значение которое должно хранится в колонке
        status.setCellValueFactory(new PropertyValueFactory<VM, Circle>("status_circle"));
        name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
        ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
        zone.setCellValueFactory(new PropertyValueFactory<VM, String>("zone"));
        info.setCellValueFactory(new PropertyValueFactory<VM, Button>("info"));

        // заполняем таблицу данными
        table.setItems(VMsList);
    }

    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(Request.get("http://167.99.138.88:8000/"+type+"/"+accName+"/allvms"));
            for (int i = 0; i < json.size(); i++) {
                VMsList.add(new VM((JSONObject) json.get(i), type, accName));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void VMsTableWindow() throws IOException {
        Stage accountStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("vms.fxml")));
        accountStage.setTitle("DFteam - VMs");
        accountStage.getIcons().add(new Image("/images/DF.png"));
        accountStage.setScene(new Scene(root));
        accountStage.show();
    }
}
