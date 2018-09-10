package com.dfteam.desktop.util;

import java.awt.*;

public class TrayNotification {
    public static void showNotification(String head) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/DF.png");
            TrayIcon trayIcon = new TrayIcon(image);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(head, "", TrayIcon.MessageType.INFO);
        }
    }
}
