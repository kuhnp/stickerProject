package com.example.rrhg5930.stickerproject.asynctask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.database.StickerContentProvider;
import com.example.rrhg5930.stickerproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pierre on 25/03/2015.
 */
public class AcceptFriendTask extends AsyncTask<URL, Integer, Long> {

    StickerApp application;
    SharedPreferences sharedPref;
    Context context;
    String friendName;
    int err = 0;

    public AcceptFriendTask (StickerApp application, SharedPreferences sharedPreferences, Context context, String friendName){
        this.application = application;
        this.sharedPref = sharedPreferences;
        this.context = context;
        this.friendName = friendName;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(URL... arg0) {

        JSONObject response = application.stickerRest.acceptFriendRequest(friendName, sharedPref.getString("token", ""));
        if (response!=null) {
            try {
                if (response.getString("type") != "true")
                    err = 1;
                else
                    err = 0;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        else{
            Log.d("FriendActivity", "JSON response is null");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        if(err == 1){
            Toast toast = Toast.makeText(context,"Error when accepting friends",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            //save  friend in db
            ContentValues values = new ContentValues();
            values.put("name", friendName);
            context.getContentResolver().insert(StickerContentProvider.FRIENDS_CONTENT_URI,values);
        }
    }
}

