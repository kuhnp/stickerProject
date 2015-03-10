package com.example.rrhg5930.stickerproject.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.gcm.GcmBroadcastReceiver;
import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by pierre on 10/03/2015.
 */
public class GcmIntentService extends IntentService {

    private static String TAG = "GcmIntentService";


    public GcmIntentService() {
        super("GcmIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e;
        e = sharedPreferences.edit();



        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                        //extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                String fromUser = intent.getStringExtra("from");
                String url = "http://10.0.1.69:8080/sticker";
                String imagePath = StickerUtil.downloadFile(url,getApplicationContext(),sharedPreferences.getString("token",""));
                e.putString("imagePath", imagePath);
                e.commit();

                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }



}
