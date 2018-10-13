package com.dfteam.desktop.util;

import javafx.application.Platform;
import javafx.util.Duration;
import tray.notification.NotificationType;

/**
 * Class for notifications in tray
 */
public class Notification {

    /**
     * @param head text of notification
     */
    public static void showWarningNotification(String head) {
        try {
            Platform.runLater(() -> {
                        tray.notification.TrayNotification tray = new tray.notification.TrayNotification(head, "", NotificationType.NOTICE);
                        tray.showAndDismiss(Duration.seconds(3d));
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessNotification(String head) {
        try {
            Platform.runLater(() -> {
                        tray.notification.TrayNotification tray = new tray.notification.TrayNotification(head, "", NotificationType.SUCCESS);
                        tray.showAndDismiss(Duration.seconds(3d));
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorNotification( String head ){
        try {
            Platform.runLater(() -> {
                        tray.notification.TrayNotification tray = new tray.notification.TrayNotification(head, "", NotificationType.ERROR);
                        tray.showAndDismiss(Duration.seconds(3d));
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
