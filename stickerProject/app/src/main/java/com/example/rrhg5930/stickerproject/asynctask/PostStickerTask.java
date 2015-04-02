package com.example.rrhg5930.stickerproject.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.StickerApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by pierre on 25/03/2015.
 */
public class PostStickerTask extends AsyncTask<URL, Integer, Long> {

    private StickerApp application;
    private SharedPreferences sharedPref;
    private Context context;
    private String friendName;
    private String imagePath;
    private int position;
    int err = 0;

    public PostStickerTask(String name, String path, StickerApp application, SharedPreferences sharedPreferences, Context context, int position){
        this.friendName = name;
        this.imagePath = path;
        this.application = application;
        this.sharedPref = sharedPreferences;
        this.context = context;
        this.position = position;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(URL... arg0) {

        JSONObject response = application.stickerRest.postSticker(this.friendName, sharedPref.getString("token", ""), this.imagePath);
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
            Toast toast = Toast.makeText(context,"Error when posting sticker",Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            ((MainActivity)context).setPosition(this.position);
        }
    }
}

