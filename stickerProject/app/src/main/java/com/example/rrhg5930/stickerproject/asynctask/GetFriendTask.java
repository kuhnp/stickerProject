package com.example.rrhg5930.stickerproject.asynctask;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pierre on 25/03/2015.
 */
public class GetFriendTask extends AsyncTask<URL, Integer, Long> {

    StickerApp application;
    SharedPreferences sharedPref;
    Context context;
    ArrayList friendList;
    ArrayList pendingFriendList;

    public GetFriendTask(StickerApp application, SharedPreferences sharedPreferences, Context context){
        this.application = application;
        this.sharedPref = sharedPreferences;
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(URL... arg0) {

        JSONObject response = application.stickerRest.getFriends(sharedPref.getString("token",""));
        if (response!=null) {
            JSONArray friendArray = null;
            JSONObject friend = null;
            friendList = new ArrayList<String>();
            pendingFriendList = new ArrayList<String>();
            try {
                friendArray = response.getJSONArray("friends");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Log.d("FriendActivity", "lenght" + friendArray.length());
            User user = User.getInstance();
            for (int i = 0; i < friendArray.length(); i++) {

                try {
                    friend = friendArray.getJSONObject(i);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    String username = friend.getString("username");
                    String isFriend = friend.getString("isFriend");
                    String fromUser = friend.getString("fromUser");



                    if(isFriend.equalsIgnoreCase("true"))
                        user.addFriend(username);

                    else if (isFriend.equalsIgnoreCase("false") && fromUser.equalsIgnoreCase("false"))
                        user.addPendingFriend(username);


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else{
            Log.d("FriendActivity", "Null friends");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
