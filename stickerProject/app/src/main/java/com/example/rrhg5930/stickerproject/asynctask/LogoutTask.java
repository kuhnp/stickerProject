package com.example.rrhg5930.stickerproject.asynctask;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.rrhg5930.stickerproject.StickerApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pierre on 19/04/2015.
 */
public class LogoutTask extends AsyncTask{

    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private int err = 0;

    @Override
    protected Long doInBackground(Object[] params) {

        JSONObject response = application.stickerRest.logout(sharedPreferences.getString("token",""));
        try {
            if (response.getString("type").equalsIgnoreCase("true")){
                err = 0;
            }
            else{
                err = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
