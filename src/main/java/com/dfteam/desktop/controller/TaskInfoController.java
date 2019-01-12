package com.dfteam.desktop.controller;

import com.dfteam.apisdk.util.TODOabs.Status;
import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import com.dfteam.desktop.util.ConfirmationDialog;
import com.dfteam.desktop.util.LocalTODO;
import com.dfteam.desktop.util.Notification;
import com.dfteam.desktop.util.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import java.io.*;

public class TaskInfoController {

    private static String id = "";
    public static void setId(String ID) { id = ID; }

    private static boolean isLocal = false;
    public static void setIsLocal(boolean local) { isLocal = local; }

    @FXML
    private Text nameText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text priorityText;

    @FXML
    private Text statusText;

    @FXML
    private Text deadlineText;

    @FXML
    private ComboBox<String> statusSelect;

    @FXML
    private Button okBtn;

    @FXML
    private Button deleteBtn;

    private long deleteClickTime = 0;

    private TODOabs todo;

    @FXML
    private void initialize() {
        if(isLocal){
            File file = new File(Main.HOME_DIR.getPath()+File.separator
                    +"todo"+File.separator+Main.customer.getLogin()+"_"+id);
            if(file.exists()) {
                try {
                    FileInputStream fio = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fio);
                    todo = (LocalTODO)ois.readObject();
                    System.out.println("LOCAL");
                    ois.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            todo = Main.customer.getTODObyID(id);
            System.out.println("GLOBAL");
        }
        nameText.setText("Name: " + todo.getName());
        descriptionText.setText("Description: " + todo.getDescription());
        priorityText.setText("Priority: " + todo.getPriority());
        statusText.setText("Status: " + todo.getStatus());
        deadlineText.setText("Deadline: " + todo.getFinishTime());

        statusSelect.setEditable(false);
        statusSelect.getItems().addAll("CREATE", "IN_PROGRESS", "FINISH", "IMPOSSIBLE", "HAVE_QUESTIONS");

        okBtn.setOnAction(event -> {
            if(statusSelect.getValue() == null)
                Notification.showWarningNotification("Please, choose new status");
            else {
                if (isLocal) {
                    todo.setStatus(TODOabs.Status.valueOf(statusSelect.getValue()));
                    File file = new File(Main.HOME_DIR.getPath()+File.separator
                            +"todo"+File.separator+todo.getLogin()+"_"+id);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(todo);
                        oos.flush();
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Main.customer.getTODObyID(id).setStatus(Status.valueOf(statusSelect.getValue()));
                    statusText.setText("Status: " + statusSelect.getValue());
                }
                Notification.showSuccessNotification("Status is successfully changed!");
            }
        });

        deleteBtn.setOnAction(event -> {
            if (ConfirmationDialog.showConfirmation("Delete task", "Are you sure want to delete this task?")) {
                if (deleteClickTime == 0 || (System.currentTimeMillis() - deleteClickTime > Main.CLICKTIME)) {
                    deleteClickTime = System.currentTimeMillis();
                    if (isLocal) {
                        if ( todo.delete()!=null ) {
                            Notification.showSuccessNotification("Local task is successfully deleted!");
                            StageManager.hideTaskInfo();
                        } else Notification.showWarningNotification("Can't delete task!");

                    } else {
                        if (Main.customer.getTODObyID(id).delete() != null) {
                            Notification.showSuccessNotification("Task is successfully deleted!");
                            StageManager.hideTaskInfo();
                        } else Notification.showWarningNotification("Can't delete task!");
                    }
                }
            }
        });
    }

}