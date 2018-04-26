package com.example.sidhant.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle data = this.getIntent().getExtras();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MessageFragment f = new MessageFragment();
        f.setArguments(data);

        ft.replace(R.id.emptyFrame, f);
        ft.commit();
    }


}
