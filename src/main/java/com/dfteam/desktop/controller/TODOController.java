package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.io.IOException;

public class TODOController {

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ListView<TODOabs> currentList;

    @FXML
    private ListView<TODOabs> delayedList;

    @FXML
    private ListView<TODOabs> finishedList;

    @FXML
    private void initialize() {
        refresh();
        currentList.setCellFactory(callback -> new TODOcell());
        delayedList.setCellFactory(callback -> new TODOcell());
        finishedList.setCellFactory(callback -> new TODOcell());
        refreshBtn.setOnAction( event -> refresh() );
        addTaskBtn.setOnAction( event -> {
            try {
                StageManager.CreateTaskStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void refresh(){
        currentList.getItems().clear();
        delayedList.getItems().clear();
        finishedList.getItems().clear();
        currentList.getItems().addAll(Main.customer.getCurrentTODO());
        delayedList.getItems().addAll(Main.customer.getDelayedTODO());
        finishedList.getItems().addAll(Main.customer.getFinishedTODO());
    }

    private class TODOcell extends ListCell<TODOabs> {
        private HBox hBox;
        private Text name;
        private Button detailsBtn;

        TODOcell(){
            super();
            name = new Text();
            detailsBtn = new Button("Details");
            hBox = new HBox(30, name, detailsBtn);
            hBox.setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(TODOabs todo, boolean empty){
            super.updateItem(todo, empty);
            if(empty || todo == null) setGraphic(null);
            else {
                name.setText(todo.getName());
                detailsBtn.setOnAction( event -> {
                    TaskInfoController.setId(todo.getId());
                    try {
                        StageManager.TaskInfoStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                setGraphic(hBox);
            }
        }
    }

}