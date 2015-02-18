package com.example.rrhg5930.stickerproject;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by pierre on 13/02/2015.
 */
public class StickerApp extends Application{

    private static final String CAMERA_PATH = "camera_path";
    private static final String MY_PREFS = "SHINDIG_PREF";
    private String cameraImagePath;
    SharedPreferences settings;
    SharedPreferences.Editor e;



    public void onCreate()
    {
        settings = getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE);
        e = settings.edit();
    }

    public void setCameraPath(String cameraPath)
    {
        e.putString(CAMERA_PATH,cameraPath);
        e.commit();
        this.cameraImagePath = cameraPath;
    }
}
