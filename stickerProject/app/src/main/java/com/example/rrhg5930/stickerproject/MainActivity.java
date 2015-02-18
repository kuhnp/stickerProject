package com.example.rrhg5930.stickerproject;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.rrhg5930.stickerproject.util.StickerUtil;


public class MainActivity extends ActionBarActivity {

    private Uri mImagePath;
    private Uri fileUri;
    ImageView mainImage;
    public StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();
        mainImage = (ImageView) findViewById(R.id.button);
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
