package com.spotishare.frey.controller.spotify;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MySpotifyPlayer implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static Player mySpotifyPlayer = null;
    public static String OAuth = "";


    protected MySpotifyPlayer(Player someSpotifyPlayer) throws PlayerAlreadyExistsException {

        if (mySpotifyPlayer == null) {
            mySpotifyPlayer = someSpotifyPlayer;
            mySpotifyPlayer.addConnectionStateCallback(MySpotifyPlayer.this);
            mySpotifyPlayer.addNotificationCallback(MySpotifyPlayer.this);
        } else
            throw new PlayerAlreadyExistsException("Player already exists");
    }

    public static Player getMySpotifyPlayer(){
        return mySpotifyPlayer;
    }

    static PlaybackState getPlaybackState(){
        return mySpotifyPlayer.getPlaybackState();
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {
    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }



    public class PlayerAlreadyExistsException extends Throwable {
        PlayerAlreadyExistsException(String message){
            super(message);
        }
    }
}
