package com.dfteam.desktop.util;

import com.dfteam.desktop.controller.AccountController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TokenChecker {

    private static File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private static File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");

    public static boolean isValid(){
        ConnectCheck.check();
        JSONObject json;
        JSONParser parser = new JSONParser();
        if(ConfigFile.exists()){
            try {
                FileReader fileReader = new FileReader(ConfigFile);
                json = (JSONObject) parser.parse(fileReader);
                fileReader.close();

                try {
                    AccountController.token = (String) json.get("token");
                    parser = new JSONParser();
                    json = (JSONObject) parser.parse(Request.get("http://167.99.138.88:8000/authcheck"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return (boolean)json.get("valid");
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void notValidMessage(){
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("images/DF.png");
            TrayIcon trayIcon = new TrayIcon(image);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage("Token is not valid",
                    "", TrayIcon.MessageType.INFO);
        }
    }
}
