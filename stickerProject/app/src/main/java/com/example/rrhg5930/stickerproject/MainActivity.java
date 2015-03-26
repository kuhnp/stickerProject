package com.example.rrhg5930.stickerproject;


import android.app.DownloadManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rrhg5930.stickerproject.adapter.TabsPagerAdapter;
import com.example.rrhg5930.stickerproject.fragment.FriendListFragment;
import com.example.rrhg5930.stickerproject.fragment.MainStickerFragment;
import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static String TAG = "Main activity";

    private ActionBar mActionBar;
    private ViewPager mViewpager;

    /*************   GCM   **************/
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = StickerConfig.SENDER_ID;

    private String mImagePath;
    //private Uri fileUri;
    ImageView mainImage;
    public StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;
    Button button;
    Button bFriend;
    ImageLoader imLoader;
    public String url_temp = StickerConfig.PARAM_URL+"/sticker";

    // friend Activity
    ArrayList<String> friendList;
    ArrayList<String> pendingFriendList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (StickerApp) getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();


        mViewpager = (ViewPager) findViewById(R.id.pager);
        mActionBar = getSupportActionBar();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("token",sharedPref.getString("token",""));
        application.setupImageLoader(headers);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainStickerFragment());
        fragments.add(new FriendListFragment(MainActivity.this));

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(),fragments);
        mViewpager.setAdapter(tabsPagerAdapter);

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab mTabMain = mActionBar.newTab().setText("My sticker").setTabListener(this);
        ActionBar.Tab mTabFriend = mActionBar.newTab().setText("friends").setTabListener(this);
        mActionBar.addTab(mTabMain);
        mActionBar.addTab(mTabFriend);
        mViewpager.setCurrentItem(0);






        //imLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        mainImage = (ImageView) findViewById(R.id.button);
        bFriend = (Button) findViewById(R.id.bFriend);

        StickerUtil.createMediaDirectory();

        bFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetFriendTask task = new GetFriendTask();
                //task.execute();

            }
        });

        if(checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = application.getRegistrationId(getApplicationContext());
            if (regid.isEmpty()){
                registerInBackground();
            }
        }
        else {
            Log.d(TAG, "No valid Google Play Services APK found.");
        }


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
                //fileUri = StickerUtil.getOutputMediaFileUri(StickerUtil.MEDIA_TYPE_IMAGE);
                //application.setCameraPath(fileUri.getPath());
               // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

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

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token",sharedPref.getString("token",""));
                application.setupImageLoader(headers);
                imLoader = ImageLoader.getInstance();
                //display the image from the url
                imLoader.displayImage(url_temp,mainImage);


        //save the image from url
        //downloadFile(url_temp);



            }
        });

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent result)
    {
        if((requestCode == 1) && (resultCode == RESULT_OK))
        {
            Uri imageUri = result.getData();
            mImagePath = StickerUtil.getRealPathFromURI(imageUri,getApplicationContext());

            FriendAdapter.mImagePath = mImagePath;
            //mImagePath = result.getData();

            //mainImage.setImageURI(mImagePath);

//            e.putString("imagePath", mImagePath.toString());
//            e.commit();
//            Log.d("path", "path = "+sharedPref.getString("imagePath",""));

//            Intent intent = new Intent(this,ExampleAppWidgetProvider.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(),ExampleAppWidgetProvider.class));
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
//            sendBroadcast(intent);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public class GetFriendTask extends AsyncTask<URL, Integer, Long> {

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
                Log.d("FriendActivity","lenght"+friendArray.length());
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

                        friendList.add(username);

//                        if(isFriend.equalsIgnoreCase("true"))
//                            friendList.add(username);
//                        else if (isFriend.equalsIgnoreCase("false") && fromUser.equalsIgnoreCase("false"))
//                            pendingFriendList.add(username);


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

            goToFriendActivity(friendList);
        }
    }




    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }





    public void goToFriendActivity(ArrayList friendList) {
        Intent intent = new Intent(this, FriendActivity.class);
        intent.putStringArrayListExtra("friendList", friendList);
        intent.putStringArrayListExtra("pendingFriendList",pendingFriendList);
        startActivity(intent);
        finish();
    }


    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    Log.d(TAG,"Registration in progress...");
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    Log.d(TAG,"Registration id = "+regid);

                    application.stickerRest.sendRegistrationIdToBackend(regid,sharedPref.getString("token",""));


                    application.storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute();

    }
}
