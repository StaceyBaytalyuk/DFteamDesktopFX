package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.LocalTODO;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.io.*;

public class TODOController {

    @FXML
    private Button addTaskBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ListView<TODOabs> localList;

    @FXML
    private ListView<TODOabs> currentList;

    @FXML
    private ListView<TODOabs> delayedList;

    @FXML
    private ListView<TODOabs> finishedList;

    @FXML
    private void initialize() {
        refresh();
        localList.setCellFactory(callback -> new TODOcell());
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
        localList.getItems().clear();
        currentList.getItems().clear();
        delayedList.getItems().clear();
        finishedList.getItems().clear();
        currentList.getItems().addAll(Main.customer.getCurrentTODO());
        delayedList.getItems().addAll(Main.customer.getDelayedTODO());
        finishedList.getItems().addAll(Main.customer.getFinishedTODO());

        File file = new File(Main.HOME_DIR.getPath()+File.separator+"todo");
        LocalTODO temp;
        if ( file.exists() ) {
            for(File file1 : file.listFiles()) {
                if(file1.getName().split("_")[0].equals(Main.customer.getLogin())) {
                    try {
                        FileInputStream fio = new FileInputStream(file1);
                        ObjectInputStream ois = new ObjectInputStream(fio);
                        temp = (LocalTODO)ois.readObject();
                        if(temp != null) localList.getItems().add(temp);
                        temp = null;
                        ois.close();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class TODOcell extends ListCell<TODOabs> {
        private HBox hBox;
        private Text name;
        private Button detailsBtn;

        TODOcell(){
            super();
            name = new Text();
            detailsBtn = new Button("Details");
            hBox = new HBox(20, detailsBtn, name);
        }

        @Override
        protected void updateItem(TODOabs todo, boolean empty){
            super.updateItem(todo, empty);
            if(empty || todo == null) setGraphic(null);
            else {
                name.setText(todo.getName());
                detailsBtn.setOnAction( event -> {
                    TaskInfoController.setId(todo.getId());
                    System.out.println(todo.getClass().getName());
                    if(todo.getClass().getName().equals("com.dfteam.desktop.util.LocalTODO")) {
                        System.out.println("true");
                        TaskInfoController.setIsLocal(true);
                    } else {
                        System.out.println("false");
                        TaskInfoController.setIsLocal(false);
                    }
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