package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
 public static final String MY_TAG = "MY CUSTOM MESSAGE";
    protected String defaultEmail = "email@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(MY_TAG, "onCreate");


         final SharedPreferences email = getSharedPreferences("User",MODE_PRIVATE);
        final EditText edit = (EditText) findViewById(R.id.Login);



        Button b = (Button) findViewById(R.id.button1);
        email.getString("DefaultEmail",defaultEmail);
       b.setOnClickListener(new View.OnClickListener() {

                  @Override
                   public void onClick(View v) {
                String i = edit.getEditableText().toString();
                      SharedPreferences.Editor writer = email.edit();
                       writer.putString("d","sid.sidhant12@gmail.com");
                       writer.commit();
                      String email2 =   email.getString("d","");
                      edit.setText(email2);

                       Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                       startActivity(intent);
                   }
                   });


    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(MY_TAG,"onResume");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(MY_TAG,"onStart");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(MY_TAG,"onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(MY_TAG,"onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(MY_TAG,"onDestroy");
    }
}
