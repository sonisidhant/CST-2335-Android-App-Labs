package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
public static final String MY_TAG="MY CUSTOM MESSAGE";
 static final int REQUEST_IMAGE_CAPTURE = 1;
    CheckBox cb;
    ImageButton imageb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(MY_TAG,"onCreate");

       imageb =(ImageButton) findViewById(R.id.ImageButton);
       imageb.setOnClickListener(new View.OnClickListener(){

    @Override
    public void onClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }}
       });

           Switch sw = (Switch) findViewById(R.id.SwitchButton);

    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   if (isChecked) {
                       CharSequence text = "Switch is on";
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(ListItemsActivity.this, text, duration); //this is the ListActivity
                       toast.show();
                   } else {
                       CharSequence text = "Switch is off";
                       int Duration = Toast.LENGTH_LONG;
                       Toast toast = Toast.makeText(ListItemsActivity.this, text, Duration); //this is the ListActivity
                       toast.show();
                   }
               }
           });

        final AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        cb = (CheckBox) findViewById(R.id.CheckBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
           @Override
            public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked){
               if(isChecked){
                   builder.setMessage("Do you want to finish the activity?");
                   builder.setTitle("CheckBox");
                   builder.setPositiveButton("ok", new DialogInterface.OnClickListener(){

                         public void onClick(DialogInterface diaolog, int id) {
                           Intent intent2 = new Intent();
                             intent2.putExtra("Response", "Here is my response");
                             setResult(Activity.RESULT_OK, intent2);

                             CharSequence text = "Activity is off";
                             int duration = Toast.LENGTH_SHORT;
                             Toast toast = Toast.makeText(ListItemsActivity.this,text,duration ); //this is the ListActivity
                             toast.show();
                           finish();
                       }
                   });
               }
                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                       }
                   });

                   builder.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageb.setImageBitmap(imageBitmap);
        }
    }
}
