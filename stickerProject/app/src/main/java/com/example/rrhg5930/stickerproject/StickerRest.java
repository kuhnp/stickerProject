package com.example.rrhg5930.stickerproject;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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


    public void signUp(String email,String pw){

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

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
