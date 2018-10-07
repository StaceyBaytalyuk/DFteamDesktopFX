package com.dfteam.desktop.util;

import javafx.application.Platform;
import org.controlsfx.control.Notifications;

/**
 * Class for notifications in tray
 */
public class TrayNotification {

    /**
     * @param head text of notification
     */
    public static void showNotification(String head) {
        String version = System.getProperties().get("javafx.runtime.version").toString().split("\\.")[0];
        int ver = Integer.parseInt(version);
        if(ver>8) {
            try {
                Platform.runLater(() ->
                Notifications.create()
                        .text("     " + head)
                        .title("                  \n") //for good look
                        .showWarning()
                );
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            System.out.println("Tray is not supported!");
        }
    }
}
