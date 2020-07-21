package com.example.secondproject;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            request.flush();
            request.close();

            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {

        for(Map.Entry<String,String> map_entry : params.entrySet()){


            stringBuilder.append("--" + boundary + "\r\n");
            stringBuilder.append("Content-Disposition: form-data; name=\"" + map_entry.getKey() + "\"\r\n\r\n");
            if (map_entry.getKey() == "name" || map_entry.getKey() == "dowhat"){
                byte[] data_Bytes = new byte[0];
                try {
                    data_Bytes = map_entry.getValue().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                java.util.Base64.Encoder encoder = Base64.getEncoder();
                String data_encoded = encoder.encodeToString(data_Bytes);
                System.out.println(encoder.encodeToString(data_Bytes));
                stringBuilder.append(data_encoded + "\r\n");
                System.out.println("data_encoded:" + data_encoded);
            }
            else {
                stringBuilder.append(map_entry.getValue() + "\r\n");
            }

        }


        //stringBuilder.append("--" + boundary + "--\r\n");

        Result = stringBuilder.toString();
        return Result ;
    }

    public String sendPostFile(String requestURL, HashMap<String, String> postDataParams, Bitmap bit_img) {
        StringBuilder sb = new StringBuilder();
        URL url;
        try {
            System.setProperty("http.proxyHost", "proxy.example.com");
            System.setProperty("http.proxyPort", "8080");
            url = new URL(requestURL);
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
            request.writeBytes(getPostFile(postDataParams));

            request.writeBytes(twoHyphens + boundary + crlf);

            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    "File" + "\";filename=\"" +
                    "img_test_g.jpg" + "\"" + crlf +
                    "Content-Type: image/jpg" + crlf);
            request.writeBytes(crlf);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bit_img.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            byte[] pixels = stream.toByteArray();
            //String img1 = Base64.encodeToString(pixels, Base64.DEFAULT);
            request.write(pixels);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + "--\r\n");

            request.flush();
            request.close();

            os.close();

            System.out.println("asdfasdfasdfasdfasdfasdf");

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("result_send_file", "log + "+sb.toString());
        System.out.println(sb.toString());
        return sb.toString();
    }


    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPostFile(HashMap<String, String> params) throws UnsupportedEncodingException {

        for(Map.Entry<String,String> map_entry : params.entrySet()){

            stringBuilder.append("--" + boundary + "\r\n");

            stringBuilder.append("Content-Disposition: form-data; name=\"" + map_entry.getKey() + "\"\r\n\r\n");

            if (map_entry.getKey() == "name"){
                byte[] data_Bytes = new byte[0];
                try {
                    data_Bytes = map_entry.getValue().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                java.util.Base64.Encoder encoder = Base64.getEncoder();
                String data_encoded = encoder.encodeToString(data_Bytes);
                System.out.println(encoder.encodeToString(data_Bytes));
                stringBuilder.append(data_encoded + "\r\n");
                System.out.println("data_encoded:" + data_encoded);
            }
            else {
                stringBuilder.append(map_entry.getValue() + "\r\n");
            }
        }
        Result = stringBuilder.toString();
        return Result;
    }

}