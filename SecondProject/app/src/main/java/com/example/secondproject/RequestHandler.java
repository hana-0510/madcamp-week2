package com.example.secondproject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {

    private String Result ;
    private StringBuilder stringBuilder = new StringBuilder();
    String FinalHttpData = "";

    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        try {
            System.setProperty("http.proxyHost", "proxy.example.com");
            System.setProperty("http.proxyPort", "8080");
            url = new URL(requestURL);
//            Log.d("d", "log5");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                FinalHttpData = br.readLine();
            } else {
                FinalHttpData = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("d", "log"+FinalHttpData);
        int a = FinalHttpData.indexOf("result");
        String result = FinalHttpData.substring(a+8, a+9);
        Log.d("d", "log"+result);
        return result;
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        for(Map.Entry<String,String> map_entry : params.entrySet()){
            stringBuilder.append("&");
            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(map_entry.getValue(), "UTF-8"));
        }
        Result = stringBuilder.toString();
        return Result ;
    }
}
