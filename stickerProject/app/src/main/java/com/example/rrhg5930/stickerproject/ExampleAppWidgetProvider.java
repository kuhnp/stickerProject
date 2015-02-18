package com.example.rrhg5930.stickerproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by pierre on 14/02/2015.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {

    SharedPreferences sharedPref;
    DateFormat df = new SimpleDateFormat("hh:mm:ss");

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String imagePath = sharedPref.getString("imagePath","");
        Log.d("Widget:", "imagePath = "+imagePath);

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Log.d("Widget", "AppWigetID = "+appWidgetId);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // To update a label
            //views.setTextViewText(R.id.widget1label, df.format(new Date()));
            if(imagePath != "")
            views.setImageViewUri(R.id.imageView,Uri.parse(imagePath));


            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
