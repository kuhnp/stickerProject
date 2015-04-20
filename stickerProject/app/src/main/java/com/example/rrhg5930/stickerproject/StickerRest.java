package com.example.rrhg5930.stickerproject;

import android.content.Context;

import com.example.rrhg5930.stickerproject.util.StickerUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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


    private JSONArray parseAnswerArray(HttpResponse httpResponse)
            throws Exception {

        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        JSONArray response = null;
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
                    response = new JSONArray(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;

    }



    public JSONObject signUp(String email,String pw, String username){

        String url = StickerConfig.PARAM_URL+"/signup";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            jsonObject.put("email", email);
            jsonObject.put("password",pw);
            jsonObject.put("username", username);
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


    public JSONObject signIn(String email,String pw){

        String url = StickerConfig.PARAM_URL+"/login";
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

    public JSONObject getFriends(String token){

        String url = StickerConfig.PARAM_URL+"/friend";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setHeader("token",token);
            HttpResponse httpResponse = httpClient.execute(httpGet,localContext);
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

    public JSONObject sendFriendInvite(String username, String token){

        String url = StickerConfig.PARAM_URL+"/friend";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            jsonObject.put("friend",username);
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setHeader("Content-type","application/json");
            httpPost.setHeader("token",token);
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

    public JSONObject postSticker(String username, String token, String localPath){

        String url = StickerConfig.PARAM_URL+"/sticker";
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setHeader("token",token);
            httpPost.setHeader("dest", username);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Content-type","multipart/form-data"));
            if(localPath != null)
                nameValuePairs.add(new BasicNameValuePair("image", localPath));

            //nameValuePairs.add(new BasicNameValuePair("dest", username));

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(NameValuePair nameValuePair : nameValuePairs){
                if(nameValuePair.getName().equalsIgnoreCase("image")){
                    File file = StickerUtil.compressImage(nameValuePair.getValue(),StickerUtil.getImageCompressPath(username));
                    entity.addPart(nameValuePair.getName(),new FileBody(file, "image/jpg"));
                }

            }


            httpPost.setEntity(entity);
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

    public JSONObject sendRegistrationIdToBackend(String regId, String token){

        String url = StickerConfig.PARAM_URL+"/notificationId";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            jsonObject.put("registration_id", regId);
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setHeader("Content-type","application/json");
            httpPost.setHeader("token",token);
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

    public JSONObject acceptFriendRequest( String friendName, String token){

        String url = StickerConfig.PARAM_URL+"/friendaccept";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            jsonObject.put("friend", friendName);
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setHeader("Content-type","application/json");
            httpPost.setHeader("token",token);
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

    public JSONObject logout(String token){

        String url = StickerConfig.PARAM_URL+"/logout";
        JSONObject jsonObject = new JSONObject();
        HttpClient httpClient = getSpecialClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
            httpPost.setHeader("Content-type","application/json");
            httpPost.setHeader("token",token);
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
