package com.spotishare.frey.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class BroadcastListenActivity extends AppCompatActivity {

    public BroadcastListenActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_listen);
        ButterKnife.inject(this);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
