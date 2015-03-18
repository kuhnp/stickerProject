package com.example.rrhg5930.stickerproject;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.util.StickerUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;


public class FriendActivity extends ActionBarActivity {

    ArrayList<String> friendUsernameList;
    private StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;
    Button sendB;
    Button refreshB;
    EditText friendET;
    int err=0;
    ListView friendListView;
    private Uri fileUri;
    private String mImagePath;
    String  selectedFriend;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friend);
        application = (StickerApp) getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();

        //////////  Get Friend List from previous Activity  ///////////
        Intent intent = getIntent();
        final ArrayList<String> friendList = intent.getStringArrayListExtra("friendList");


        //////////  Initialize UI  //////////
        sendB = (Button) findViewById(R.id.buttonSendFriendRequest);
        refreshB = (Button) findViewById(R.id.buttonRefresh);
        friendET = (EditText) findViewById(R.id.addFriendsET);

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFriendRequest postFriendRequest = new PostFriendRequest();
                postFriendRequest.execute();
            }
        });

        refreshB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FriendAdapter(friendList);
        mRecyclerView.setAdapter(mAdapter);


//        //// Update UI ////
//        FriendListAdapter adapter = new FriendListAdapter(friendList,getApplicationContext());
//        friendListView = (ListView) findViewById(R.id.listView);
//        friendListView.setAdapter(adapter);
//        friendListView.setItemsCanFocus(false);
//
//
//
//        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedFriend = friendList.get(position);
//                Log.d("firend", "selected friend = "+selectedFriend);
//                chosePicture();
//
//            }
//        });

        Button sendPic;
        sendPic = (Button) findViewById(R.id.sendPic_btn);

        sendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostSticker task = new PostSticker(selectedFriend,mImagePath);
                task.execute();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
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





    public class PostFriendRequest extends AsyncTask<URL, Integer, Long> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(URL... arg0) {

            JSONObject response = application.stickerRest.sendFriendInvite(friendET.getText().toString(), sharedPref.getString("token", ""));
            try {
                if (response.getString("type") != "true")
                    err = 1;
                else
                    err = 0;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Long result) {
            if(err == 1){
                Toast toast = Toast.makeText(getApplicationContext(),"Error when inviting friends",Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }

//    void refreshFriendList(){
//        ListView friendList = (ListView) findViewById(R.id.listView);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friendUsernameList);
//        friendList.setAdapter(arrayAdapter);
//    }

    public void chosePicture(){

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

    protected void onActivityResult(int requestCode,int resultCode,Intent result)
    {
        if((requestCode == 1) && (resultCode == RESULT_OK))
        {
            Uri imageUri = result.getData();
            mImagePath = StickerUtil.getRealPathFromURI(imageUri,getApplicationContext());
            e.putString("imagePathTmp", mImagePath);
            e.commit();
            Log.d("path", "path = "+sharedPref.getString("imagePathTmp",""));

        }

    }


    public class PostSticker extends AsyncTask<URL, Integer, Long> {

        private String friendName;
        private String imagePath;

        PostSticker(String name, String path){
            this.friendName = name;
            this.imagePath = path;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(URL... arg0) {

            JSONObject response = application.stickerRest.postSticker(this.friendName, sharedPref.getString("token", ""), this.imagePath);
            try {
                if (response.getString("type") != "true")
                    err = 1;
                else
                    err = 0;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Long result) {
            if(err == 1){
                Toast toast = Toast.makeText(getApplicationContext(),"Error when inviting friends",Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }



}
