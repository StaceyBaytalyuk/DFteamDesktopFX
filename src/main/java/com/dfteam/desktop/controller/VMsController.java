package com.dfteam.desktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VMsController {

    private ObservableList<VM> usersData = FXCollections.observableArrayList();

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, String> status;

    @FXML
    private TableColumn<VM, String> name;

    @FXML
    private TableColumn<VM, String> ip;

    @FXML
    private TableColumn<VM, String> zone;

    @FXML
    private TableColumn<VM, String> type;

    @FXML
    private TableColumn<VM, String> info;



    // инициализируем форму данными
    @FXML
    private void initialize() {
        initData();

        // устанавливаем тип и значение которое должно хранится в колонке
        status.setCellValueFactory(new PropertyValueFactory<VM, String>("status"));
        name.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
        ip.setCellValueFactory(new PropertyValueFactory<VM, String>("ip"));
        zone.setCellValueFactory(new PropertyValueFactory<VM, String>("zone"));
        info.setCellValueFactory(new PropertyValueFactory<VM, String>("info"));
        type.setCellValueFactory(new PropertyValueFactory<VM, String>("type"));


        // заполняем таблицу данными
        table.setItems(usersData);
    }

    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() {
        usersData.add(new User(1, "Alex", "qwerty", "alex@mail.com"));
        usersData.add(new User(2, "Bob", "dsfsdfw", "bob@mail.com"));
        usersData.add(new User(3, "Jeck", "dsfdsfwe", "Jeck@mail.com"));
        usersData.add(new User(4, "Mike", "iueern", "mike@mail.com"));
        usersData.add(new User(5, "colin", "woeirn", "colin@mail.com"));
    }
}
