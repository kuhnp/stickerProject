package com.example.rrhg5930.stickerproject.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.StickerApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pierre on 25/03/2015.
 */
public class AddFriendTask extends AsyncTask<URL, Integer, Long> {

    StickerApp application;
    SharedPreferences sharedPref;
    Context context;
    String friendName;
    int err = 0;

    public AddFriendTask(StickerApp application, SharedPreferences sharedPreferences, Context context, String friendName){
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

        JSONObject response = application.stickerRest.sendFriendInvite(friendName, sharedPref.getString("token", ""));
        try {
            if (response.getString("type") != "true")
                err = 1;
            else
                err = 0;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Long result) {
        if(err == 1){
            Toast toast = Toast.makeText(context,"Error when inviting friends",Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
