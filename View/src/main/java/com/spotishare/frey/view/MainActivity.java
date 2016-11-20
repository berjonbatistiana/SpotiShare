package com.spotishare.frey.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotishare.frey.controller.spotify.MySpotifyPlayer;
import com.spotishare.frey.controller.spotify.SpotifyCredentials;
import com.spotishare.frey.controller.spotify.SpotifyLogin;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.spotishare.frey.controller.spotify.SpotifyWebAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private ProgressDialog spotifyLoginProgressDialog;

    @OnClick(R.id.spotifyLoginButton)
    public void spotifyLoginButtonOnClick(View v) {
        final SpotifyLogin spotifyLogin = new SpotifyLogin(this);
        spotifyLogin.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SpotifyCredentials.LOGIN_REQUEST_CODE) {
            AuthenticationResponse spotifyAuthResponse = AuthenticationClient.getResponse(resultCode, intent);
            MySpotifyPlayer.OAuth = spotifyAuthResponse.getAccessToken();

            if (spotifyAuthResponse.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, spotifyAuthResponse.getAccessToken(), SpotifyCredentials.CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {

                        try {
                            new MySpotifyPlayer(spotifyPlayer){
                                @Override
                                public void onLoggedIn() {
                                    super.onLoggedIn();
                                    startBroadcastListenActivity();
                                }
                            };

                        } catch (MySpotifyPlayer.PlayerAlreadyExistsException e) {
                            e.printStackTrace();
                            startBroadcastListenActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    private void startBroadcastListenActivity(){
        Intent broadcastListenIntent = new Intent(this, BroadcastListenActivity.class);
        this.startActivity(broadcastListenIntent);
        this.finish();

        MySpotifyPlayer.getMySpotifyPlayer().getMetadata();
        SpotifyWebAPI webAPI = new SpotifyWebAPI(MySpotifyPlayer.OAuth);
        webAPI.getMySpotifyProfile();
    }
}

