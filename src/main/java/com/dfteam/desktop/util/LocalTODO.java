package com.dfteam.desktop.util;

import com.dfteam.apisdk.util.TODOabs;
import com.dfteam.desktop.Main;
import java.io.*;

public class LocalTODO extends TODOabs {

    public LocalTODO(String login, String id, String name, String description, String deadline, TODOabs.Priority priority) {
        this.id = id;
        this.todo_name = name;
        this.todo_description = description;
        this.todo_time = deadline;
        this.todo_priority = priority;
        this.todo_status = Status.CREATE;
        this.login = login;
    }

    @Override
    public TODOabs delete() {
        File file = new File(Main.HOME_DIR.getPath()+File.separator +"todo"+File.separator+login+"_"+id);
        if ( file.exists() ) {
            System.out.println("Exists");
            if ( file.delete() ) {
                System.out.println("Delete");
                return this;
            }
        } else {
            System.out.println("Can`t open the file");
        }
        return null;
    }

    @Override
    public void setStatus(Status status) {
        this.todo_status = status;
    }

    public void serialize() {
        File file = new File(Main.HOME_DIR.getPath()+File.separator+"todo");
        if ( !file.exists() ) file.mkdir();
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath()+File.separator+login+"_"+id);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}