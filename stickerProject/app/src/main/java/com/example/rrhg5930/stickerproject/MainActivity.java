package com.example.rrhg5930.stickerproject;


import android.app.Activity;
import android.app.DownloadManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private Uri mImagePath;
    private Uri fileUri;
    ImageView mainImage;
    public StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;
    Button button;
    ImageLoader imLoader = ImageLoader.getInstance();
    public String url_temp = "http://10.0.1.56:8080/api/files";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();
        mainImage = (ImageView) findViewById(R.id.button);

        // check how many widget have been installed on the home screen
        AppWidgetManager mAppWidgetManager= AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(getApplicationContext(), ExampleAppWidgetProvider.class);
        int[] allWidgetIds2 = mAppWidgetManager.getAppWidgetIds(thisWidget);

        // keep the default image if it has not been already set
        if(sharedPref.getString("imagePath","")!= "")
            mainImage.setImageURI(Uri.parse(sharedPref.getString("imagePath","")));



        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
                galleryIntent.setType("image/*");
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = StickerUtil.getOutputMediaFileUri(StickerUtil.MEDIA_TYPE_IMAGE);
                //application.setCameraPath(fileUri.getPath());
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_INTENT,galleryIntent);
                chooser.putExtra(Intent.EXTRA_TITLE,"Choose a picture");
                Intent[] intentArray = {cameraIntent};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentArray);

                startActivityForResult(chooser, 1);
            }
        });

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display the image from the url
        imLoader.displayImage(url_temp,mainImage);

        //save the image from url
        downloadFile(url_temp);



            }
        });

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent result)
    {
        if((requestCode == 1) && (resultCode == RESULT_OK))
        {
            mImagePath = result.getData();
            mainImage.setImageURI(mImagePath);
            e.putString("imagePath", mImagePath.toString());
            e.commit();
            Log.d("path", "path = "+sharedPref.getString("imagePath",""));

            Intent intent = new Intent(this,ExampleAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(),ExampleAppWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            sendBroadcast(intent);
        }

    }

    public void downloadFile(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/AnhsirkDasarp");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/AnhsirkDasarpFiles", "fileName.jpg");

        mgr.enqueue(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
