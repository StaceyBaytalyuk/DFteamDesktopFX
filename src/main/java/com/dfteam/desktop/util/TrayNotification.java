package com.dfteam.desktop.util;

//import java.awt.*;

import org.controlsfx.control.Notifications;

public class TrayNotification {
    public static void showNotification(String head) {
        String version = System.getProperties().get("javafx.runtime.version").toString().split("\\.")[0];
        int ver = Integer.parseInt(version);
        if(ver>8) {
            try {
                Notifications.create()
                        .text("     " + head)
                        .title("                  \n") //for good look
                        .showWarning();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            System.out.println("Tray not support!");
        }
//        if (SystemTray.isSupported()) {
//            SystemTray tray = SystemTray.getSystemTray();
//            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/DF.png");
//            TrayIcon trayIcon = new TrayIcon(image);
//            try {
//                tray.add(trayIcon);
//            } catch (AWTException e) {
//                e.printStackTrace();
//            }
//            trayIcon.displayMessage(head, "", TrayIcon.MessageType.INFO);
//        }else{
//            System.out.println("Tray not support!");
//        }
    }
}
