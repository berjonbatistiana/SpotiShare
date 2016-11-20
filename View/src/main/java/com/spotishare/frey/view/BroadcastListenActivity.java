package com.spotishare.frey.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

public class BroadcastListenActivity extends AppCompatActivity {


    @InjectView(R.id.switchBroadcastListen)
    Switch switchBroadcastListen;

    @OnCheckedChanged(R.id.switchBroadcastListen)
    public void swtichBroadcastListenOnCheckedChanged(){

        switchBroadcastListen.setText(switchBroadcastListen.isChecked()?
                getResources().getString(R.string.string_broadcaster) :
                getResources().getString(R.string.string_listener));
    }

    public BroadcastListenActivity() {

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
