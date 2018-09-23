package com.dfteam.desktop.util;

import java.io.IOException;
import java.net.*;

public class ConnectCheck {

    private static String url = "http://167.99.138.88:8000";

    public static void check(){
        if (!internet()) {
            TrayNotification.showNotification("Internet connection failed");
            System.exit(0);
        }
        else if(!server()){
            TrayNotification.showNotification("Can't connect to server");
            System.exit(0);
        }
    }

    private static boolean internet(){
        //0 способ
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80), 1500);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
        //1 способ
        /*boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            result = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;*/

        //2 способ
        /*InetAddress in = null;
        try {
            in = InetAddress.getByName("google.com");
            return (in.isReachable(3000));
        } catch (IOException e) {
//            e.printStackTrace();
        }

        return false;*/
        //3 способ
        /*Process proc = Runtime.getRuntime().exec("ping -n 1 " + url);
        boolean reachable = (proc.waitFor()==0);
        System.out.println(reachable ? "Host is reachable" : "Host is NOT reachable");*/

        //4 способ
        /*try {
            final URL url = new URL(url);
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }*/
    }

    private static boolean server(){
        //1
        /*final String host = url;
        InetAddress in = null;
        try {
            in = InetAddress.getByAddress(host.getBytes());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            if( in.isReachable(1000) ) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;*/

        //2
        /*try {
            final URL Url = new URL(url);
            final URLConnection conn = Url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }*/

        //3
        boolean result = false;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
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
