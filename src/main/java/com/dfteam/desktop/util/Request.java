package com.dfteam.desktop.util;

import com.dfteam.desktop.controller.AccountController;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {
    private static JSONParser parser = new JSONParser();

    private static HttpClient client = HttpClientBuilder.create().build();

    public static String get(String URL){
        HttpGet get = new HttpGet(URL);
        get.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;
        try {
            response = client.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject post(String URL, Map<String, String> args){
        HttpPost post = new HttpPost(URL);
        post.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;

        if(args!=null){
            List<NameValuePair> arguments = new ArrayList<>(args.size());
            for (Map.Entry entry : args.entrySet()) {
                arguments.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(arguments));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            response = client.execute(post);
            return (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject delete(String URL){
        HttpDelete del = new HttpDelete(URL);
        del.addHeader("Authorization", AccountController.token);
        HttpResponse response = null;
        try {
            response = client.execute(del);
            return (JSONObject) parser.parse(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
