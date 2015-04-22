package com.example.rrhg5930.stickerproject.asynctask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.database.DbAdapter;
import com.example.rrhg5930.stickerproject.util.StickerUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pierre on 19/04/2015.
 */
public class LogoutTask extends AsyncTask{

    private StickerApp application;
    private Context context;
    private SharedPreferences sharedPreferences;
    private int err = 0;

    public LogoutTask(StickerApp application, Context context, SharedPreferences sharedPreferences){
        this.application = application;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected Long doInBackground(Object[] params) {

        JSONObject response = application.stickerRest.logout(sharedPreferences.getString("token",""));
        try {
            if (response.getString("type").equalsIgnoreCase("true")){
                err = 0;
                DbAdapter dbAdapter = new DbAdapter(application);
                dbAdapter.open();
                dbAdapter.cleanDatabase();
                dbAdapter.close();
                StickerUtil.deleteAppData(sharedPreferences.getString("username", ""));
                application.deletePreferences();
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
        if(err == 0){
            Intent i = ((Activity)context).getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( ((Activity)context).getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }
}
