package com.example.rrhg5930.stickerproject.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.rrhg5930.stickerproject.StickerApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public static File compressImage(String path, String compressPath){
        Bitmap bmp = BitmapFactory.decodeFile(path);
        File file = new File(compressPath);
        FileOutputStream out;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.close();
            bmp.recycle();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImageCompressPath(String username){
        return Environment.getExternalStorageDirectory()+"/Pictures/StickerApp/"+username+".jpg";

    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void createMediaDirectory(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "StickerApp");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d("Sticker", "failed to create directory");
            }
        }
    }

    public static String downloadFile(String uRl, Context context, String token) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/stickerAppReceived");


        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .addRequestHeader("token",token)
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/stickerAppReceived", StickerApp.mainUsername+".jpg");

                        mgr.enqueue(request);

        String path = direct.getPath()+"/"+StickerApp.mainUsername+".jpg";
        return path;

    }


}
