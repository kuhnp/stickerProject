package com.example.rrhg5930.stickerproject.util;

import android.app.DownloadManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.ExampleAppWidgetProvider;
import com.example.rrhg5930.stickerproject.StickerApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;

//        String result = contentURI.getPath();
//        return result;
    }

    public static String getPathFromFragment(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
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

    public static void deleteAppDataWhenLogin() {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stickerAppReceived");
        if(folder.exists()){
            String[] children = folder.list();
            for(int i = 0; i<children.length; i++){
                new File(folder,children[i]).delete();
            }
        }
    }
    public static void deleteAppData(String username){

        File pic = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stickerAppReceived/"+username+".jpg");
        if(pic.exists()) {
            pic.delete();
        }
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stickerAppReceived");
        if(folder.exists()){
           folder.delete();
        }
    }


    public static String downloadFile(String uRl, Context context, String token, String username) {

        /*********** Open shared preferences ************/
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        /*********** Create destination folder ************/
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/stickerAppReceived");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        /*********** Delete previous picture ************/
        File pic = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stickerAppReceived/"+username+".jpg");
        if(pic.exists())
            pic.delete();


        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);


        /*********** BroadcastReceiver to handle the onComple of download ************/
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Toast toast = Toast.makeText(ctxt,"Download is Finished",Toast.LENGTH_SHORT);
                toast.show();

                Intent newIntent = new Intent(ctxt, ExampleAppWidgetProvider.class);
                newIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                // since it seems the onUpdate() is only fired on that:
                int[] ids = AppWidgetManager.getInstance(ctxt).getAppWidgetIds(new ComponentName(ctxt,ExampleAppWidgetProvider.class));
                newIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                ctxt.sendBroadcast(newIntent);
            }
        };


        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .addRequestHeader("token",token)
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/stickerAppReceived", username+".jpg");

                        mgr.enqueue(request);


        String path = direct.getPath()+"/"+username+".jpg";
        return path;

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}
