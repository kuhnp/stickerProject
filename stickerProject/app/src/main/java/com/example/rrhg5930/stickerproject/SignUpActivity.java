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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class SignUpActivity extends ActionBarActivity {


    /*************   UI   **************/
    private Button mSignUpB;
    private EditText mPwEditText;
    private EditText mEmailEditText;
    private EditText mUsernameEditText;


    /*************   User imputs   **************/
    private String mEmail;
    private String mPwd;
    private String mUsername;

    /*************   Preferences & error   **************/
    int err = 0;
    private StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        application = (StickerApp) getApplicationContext();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();

        mSignUpB = (Button) findViewById(R.id.signUpB);
        mPwEditText = (EditText) findViewById(R.id.pwdETSignUp);
        mEmailEditText = (EditText) findViewById(R.id.emailET);
        mUsernameEditText = (EditText) findViewById(R.id.usernameETSignUp);


        mSignUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                mPwd = mPwEditText.getText().toString();
                mUsername = mUsernameEditText.getText().toString();
                if(mUsername.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username is empty",Toast.LENGTH_SHORT).show();
                }
                else if(mPwd.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_SHORT).show();
                }
                else if(mEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Email is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    SignUpTask task = new SignUpTask();
                    task.execute();
                }

            }
        });

    }


    public void goToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public class SignUpTask extends AsyncTask<URL, Integer, Long>{

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(URL... arg0) {

        JSONObject response = application.stickerRest.signUp(mEmail, mPwd, mUsername);
            try {
                if(response.getString("type") == "true"){
                    String token = response.getString("token");
                    e.putString("token", token.toString());
                    e.putString("username",mUsername);
                    e.putBoolean("isLoggedIn",true);
                    e.commit();
                    Log.d("SignUp", "token = "+token);

                    err = 0;
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
            else {
                Toast toast = Toast.makeText(getApplicationContext(),"Error when sigining up",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
