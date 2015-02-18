package com.example.rrhg5930.stickerproject.util;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pierre on 12/02/2015.
 */
public class StickerUtil {


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_AUDIO = 3;


    public static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        if(isExternalStorageWritable()){

            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Shindig");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("Shindig", "failed to create directory");
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else if (type == MEDIA_TYPE_AUDIO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "AUD_" + timeStamp + ".3gp");
            } else {
                return null;
            }

            return mediaFile;

        } else{
            return null;
        }
    }



    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
