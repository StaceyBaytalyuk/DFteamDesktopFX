package com.dfteam.desktop.util;

import com.dfteam.desktop.Main;
import java.io.IOException;
import java.net.*;

/**
 * Class for checking connection to the Internet and server.
 * Is used before something that needs to work with server
 */
public class ConnectCheck {

    /**
     * Exit program if connection to the Internet or server is failed
     */
    public static void check(){
        if (!internet()) {
            Notification.showWarningNotification("Internet connection failed");
            System.exit(0);
        }
        else if(!server()){
            Notification.showWarningNotification("Can't connect to server");
            System.exit(0);
        }
    }

    /**
     * Check Internet connection
     * @return true if it`s OK
     */
    private static boolean internet(){
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80), 1500);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    /**
     * Check connection to server
     * @return true if it`s OK
     */
    private static boolean server(){
        boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(Main.url).openConnection();
            con.setRequestMethod("GET");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception ignored) {
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

}
