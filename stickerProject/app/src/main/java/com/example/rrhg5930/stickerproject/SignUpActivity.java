package com.example.rrhg5930.stickerproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.URL;


public class SignUpActivity extends ActionBarActivity {

    Button bSend;
    EditText pwEditText;
    EditText emailEditText;
    String email;
    String pw;
    private StickerApp application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        bSend = (Button) findViewById(R.id.button5);
        pwEditText = (EditText) findViewById(R.id.editText4);
        emailEditText = (EditText) findViewById(R.id.editText3);
        application = (StickerApp) getApplicationContext();

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                pw = pwEditText.getText().toString();
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

        JSONObject aaa = application.stickerRest.signUp(email,pw);
        int b = 3;

            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
        goToMainActivity();
        }
    }
}
