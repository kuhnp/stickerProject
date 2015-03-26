package com.example.rrhg5930.stickerproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rrhg5930.stickerproject.asynctask.GetFriendTask;


public class SplashActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    StickerApp application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        application = (StickerApp) getApplicationContext();
        setContentView(R.layout.activity_splash);


        final boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(isLoggedIn){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
                else {
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 4000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
}
