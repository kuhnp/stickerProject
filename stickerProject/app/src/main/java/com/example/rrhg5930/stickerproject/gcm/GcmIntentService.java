package com.example.rrhg5930.stickerproject.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.rrhg5930.stickerproject.ExampleAppWidgetProvider;
import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.StickerConfig;
import com.example.rrhg5930.stickerproject.database.StickerContentProvider;
import com.example.rrhg5930.stickerproject.gcm.GcmBroadcastReceiver;
import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by pierre on 10/03/2015.
 */
public class GcmIntentService extends IntentService {

    private static String TAG = "GcmIntentService";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

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



            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                String fromUser = intent.getStringExtra("senderUsername");
                String type = intent.getStringExtra("messageType");

                if(type.equalsIgnoreCase("postSticker")) {

                    String url = StickerConfig.PARAM_URL+"/sticker";
                    String imagePath = StickerUtil.downloadFile(url, getApplicationContext(), sharedPreferences.getString("token", ""), sharedPreferences.getString("username",""));
                    e.putString("imagePath", imagePath);
                    e.commit();
                    sendNotification("New post on your sticker! by"+fromUser);

                }

                else if(type.equalsIgnoreCase("friendRequest")){
                    sendNotification("New friend Request! From "+fromUser);
                    // save new friend in db
                    ContentValues values = new ContentValues();
                    values.put("name", fromUser);
                    values.put("isFriend","false");
                    values.put("fromuser","false");
                    getApplicationContext().getContentResolver().insert(StickerContentProvider.FRIENDS_CONTENT_URI,values);
                }

                else if(type.equalsIgnoreCase("friendRequestAccepted")){
                    sendNotification(""+fromUser+" has accepted your invitation");
                    // update friendship in db
                    ContentValues values = new ContentValues();
                    values.put("name", fromUser);
                    values.put("isFriend","true");
                    values.put("fromuser","false");
                    String selection = "name = ?";
                    String[] params = new String[1];
                    params[0] = fromUser;
                    getApplicationContext().getContentResolver().update(StickerContentProvider.FRIENDS_CONTENT_URI, values, selection, params);
                }


                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("stickME")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


}
