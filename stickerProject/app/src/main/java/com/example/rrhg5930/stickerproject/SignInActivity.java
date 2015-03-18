package com.example.rrhg5930.stickerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class SignInActivity extends ActionBarActivity {

    private static String TAG = "Sign IN activity";


    /*************   GCM   **************/
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    /*************   UI   **************/

    private Button mBLoggin;
    private EditText mPwEditText;
    private EditText mUsernameEditText;
    private TextView mSignUpTextView;

    /*************   User imputs   **************/

    private String mUsername;
    private String mPwd;

    /*************   Preferences & error   **************/
    private int err = 0;
    private StickerApp application;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /*************   Grab resources   **************/
        application = (StickerApp) getApplicationContext();
        mBLoggin = (Button)findViewById(R.id.signUpB);
        mPwEditText = (EditText) findViewById(R.id.pwdETSignUp);
        mUsernameEditText = (EditText) findViewById(R.id.usernameETSignUp);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSignUpTextView = (TextView) findViewById(R.id.clickableTextView);
        e = sharedPref.edit();

        mBLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEditText.getText().toString();
                mPwd = mPwEditText.getText().toString();
                if(mUsername.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username is empty",Toast.LENGTH_SHORT).show();
                }
                else if(mPwd.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    SignInTask task = new SignInTask();
                    task.execute();
                }
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpActivity();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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


    public class SignInTask extends AsyncTask<URL, Integer, Long> {

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Long doInBackground(URL... arg0) {

            JSONObject response = application.stickerRest.signIn(mUsername, mPwd);
            try {
                if(response.getString("type") == "true"){
                    String token = response.getString("token");
                    if(token == null)
                        err =1;
                    else{
                        e.putString("token", token.toString());
                        e.putString("username", mUsername);
                        e.putBoolean("isLoggedIn",true);
                        e.commit();
                        Log.d("SignIn", "token = " + token);
                        err = 0;
                    }
                }
                else{
                    err = 1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long result) {

            if(err == 0)
                goToMainActivity();
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"Error when login...",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        }


    public void goToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
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

}
