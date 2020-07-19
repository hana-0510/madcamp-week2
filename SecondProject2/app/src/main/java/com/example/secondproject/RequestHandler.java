package com.example.secondproject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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

    final String boundary = "*****";
    final String crlf = "\r\n";
    final String twoHyphens = "--";

    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        StringBuilder sb = new StringBuilder();
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
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.connect();

            OutputStream os = conn.getOutputStream();


            DataOutputStream request = new DataOutputStream(os);
            request.writeBytes(getPostDataString(postDataParams));

            System.out.println(getPostDataString(postDataParams));

            request.flush();
            request.close();

            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("d", "log + "+sb.toString());
        return sb.toString();
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

        for(Map.Entry<String,String> map_entry : params.entrySet()){


            stringBuilder.append("--" + boundary + "\r\n");
            stringBuilder.append("Content-Disposition: form-data; name=\""+ map_entry.getKey() + "\"\r\n\r\n");
            stringBuilder.append(map_entry.getValue() + "\r\n");
        }

        stringBuilder.append("--" + boundary + "--\r\n");

        Result = stringBuilder.toString();
        return Result ;
    }
}