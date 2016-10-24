package com.spotishare.frey.controller.spotify;

import android.app.Activity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

/**
 * Created by frey on 10/15/16.
 */

public class SpotifyLogin {

    private Activity activity;

    public SpotifyLogin(Activity activity){
        this.activity = activity;
    }

    public void execute(){
        AuthenticationRequest.Builder spotifyAuthBuilder = new AuthenticationRequest.Builder(   SpotifyCredentials.CLIENT_ID,
                                                                                                AuthenticationResponse.Type.TOKEN,
                                                                                                SpotifyCredentials.REDIRECT_URI);

        spotifyAuthBuilder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest spotifyAuthRequest = spotifyAuthBuilder.build();
        AuthenticationClient.openLoginActivity(activity, SpotifyCredentials.LOGIN_REQUEST_CODE, spotifyAuthRequest);
    }

}
