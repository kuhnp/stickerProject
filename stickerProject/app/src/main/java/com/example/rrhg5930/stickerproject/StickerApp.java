package com.example.rrhg5930.stickerproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Map;

/**
 * Created by pierre on 13/02/2015.
 */
public class StickerApp extends Application{

    private static String TAG = "StickerApp";

    private static final String CAMERA_PATH = "camera_path";
    private String cameraImagePath;
    SharedPreferences settings;
    SharedPreferences.Editor e;
    public StickerRest stickerRest;



    public void onCreate()
    {
        //settings = getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE);
        //e = settings.edit();
        super.onCreate();
        stickerRest = StickerRest.getInstance(getApplicationContext());
    }

    public void setCameraPath(String cameraPath)
    {
        e.putString(CAMERA_PATH,cameraPath);
        e.commit();
        this.cameraImagePath = cameraPath;
    }

    public void setupImageLoader(Map<String, String> headers){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .extraForDownloader(headers)
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.image_fun)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .imageDownloader(new CustomImageDownloader(getApplicationContext()))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheExtraOptions((int) (256 * density),
                        (int) (256 * density))

                .build();

        ImageLoader.getInstance().init(config);
    }

    public String getRegistrationId(Context context){
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        String regId = settings.getString("registration_id","");
        if (regId.isEmpty()){
            Log.d(TAG,"Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = settings.getInt("app_version", Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return regId;
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void storeRegistrationId(Context context, String regId) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        int appVersion = getAppVersion(context);
        Log.d(TAG, "Saving regId on app version " + appVersion);
        e = settings.edit();
        e.putString("registration_id", regId);
        e.putInt("app_version", appVersion);
        e.commit();
    }

    public void deletePreferences() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
    }

}
