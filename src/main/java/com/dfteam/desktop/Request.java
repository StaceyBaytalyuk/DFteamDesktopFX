package com.dfteam.desktop;

import com.dfteam.desktop.controller.AccountController;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.HashMap;

public class Request {

    private static HttpClient client = HttpClientBuilder.create().build();

    public static String get(String URL){
        HttpGet get = new HttpGet("http://167.99.138.88:8000/accountlist");
        get.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.toString();
        } else return "";
    }

    public static JSONObject post(String URL, HashMap args){
        HttpPost post = new HttpPost(URL);
        post.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(args));
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return (JSONObject) response;
    }

    public static JSONObject delete(String URL){
        HttpDelete del = new HttpDelete(URL);
        del.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;
        try {
            response = client.execute(del);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (JSONObject) response;
    }


}
