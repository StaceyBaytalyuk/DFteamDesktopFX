package com.dfteam.desktop.util;

import com.dfteam.desktop.controller.AccountController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for checking if token is valid
 */
public class TokenChecker {

    private static File HomeDir = new File(System.getProperty("user.home")+File.separator+".dfteam");

    private static File ConfigFile = new File(HomeDir.getPath()+File.separator+"config.json");

    /**
     * Get token from the file in HomeDir and send it to server in order to check it
     * @return true if token is valid, false if token is not valid or does`t exit
     */
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
                    json = (JSONObject) parser.parse(Request.get("http://34.202.9.91:8000/authcheck"));
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
}
