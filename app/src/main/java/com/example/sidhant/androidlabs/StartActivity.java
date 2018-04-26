package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

    private static final String ACTIVITY_NAME = StartActivity.class.getSimpleName();
    private TextView welcome;
    private Button helloButton;
    //  private Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");


        welcome = (TextView) findViewById(R.id.helloText);
        helloButton = (Button) findViewById(R.id.buttonHello);
        SharedPreferences sharedPref = getSharedPreferences("User info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
         edit.putString("c", "sid.sidhant12@gmail.com");
        edit.commit();
        String email = sharedPref.getString("c","");

        welcome.setText(email);

        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.StartChatbutton);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindow.class);
                startActivityForResult(intent, 10);
            }
        });

        Button weatherButton = findViewById(R.id.buttonWeather);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, WeatherForecast.class);
                startActivityForResult(intent, 10);
            }
        });

        Button buttont = (Button) findViewById(R.id.toolbar);
        buttont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, TestToolbar.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
                break;
        }
        switch (resultCode) {
            case Activity.RESULT_OK:
                String messagePassed = data.getStringExtra("Response");
                Toast.makeText(this, messagePassed, Toast.LENGTH_LONG).show();
                break;
        }

    }
}
