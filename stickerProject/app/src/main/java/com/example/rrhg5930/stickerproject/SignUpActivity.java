package com.example.rrhg5930.stickerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    Button bSend;
    EditText pwEditText;
    EditText emailEditText;
    EditText usernameEditText;
    String email;
    String pw;
    String username;
    int err = 0;
    private StickerApp application;
    SharedPreferences sharedPref;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e = sharedPref.edit();

        bSend = (Button) findViewById(R.id.button5);
        pwEditText = (EditText) findViewById(R.id.editText4);
        emailEditText = (EditText) findViewById(R.id.editText3);
        usernameEditText = (EditText) findViewById(R.id.editText5);
        application = (StickerApp) getApplicationContext();

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                pw = pwEditText.getText().toString();
                username = usernameEditText.getText().toString();
                SignUpTask task = new SignUpTask();
                task.execute();


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

        JSONObject response = application.stickerRest.signUp(email,pw, username);
            try {
                if(response.getString("type") == "true"){
                    String token = response.getString("token");
                    e.putString("token", token.toString());
                    e.commit();

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
