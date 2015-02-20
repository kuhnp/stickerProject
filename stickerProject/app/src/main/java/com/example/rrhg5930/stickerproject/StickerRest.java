package com.example.rrhg5930.stickerproject;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by pierre on 20/02/2015.
 */
public class StickerRest {


    static StickerRest mInstance;
    private Context context;
    private StickerApp application;

    private StickerRest(Context context){
        this.context = context;
        application = (StickerApp) context.getApplicationContext();

    }

    public static StickerRest getInstance(Context c){
       if(mInstance == null){
           mInstance = new StickerRest(c);
       }
        return mInstance;
    }


    public DefaultHttpClient getSpecialClient()
    {
        MyHttpClient client = new MyHttpClient(application.getApplicationContext());
        client.createClientConnectionManager();
        return client;
    }

    private JSONObject parseAnswer(HttpResponse httpResponse)
            throws Exception {

        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        JSONObject response = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpEntity httpentity = httpResponse.getEntity();
        InputStream inputStream = httpentity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();

        if (statusCode == 200) {
            try {
                response = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (statusCode == 400) {


        } else {
        }
            return response;

        }


    public JSONObject signUp(String email,String pw){

        String url = "http://10.0.1.69:8080/signup";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            jsonObject.put("email", email);
            jsonObject.put("password",pw);
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setHeader("Content-type","application/json");
            httpPost.setEntity(se);
            HttpResponse httpResponse = httpClient.execute(httpPost,localContext);
            return parseAnswer(httpResponse);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
