package com.spotishare.frey.view;

import android.app.Activity;
import android.widget.Toast;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MySpotifyPlayer implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static Player mySpotifyPlayer = null;
    private static Activity activeActivity = null;
    public static String OAuth = "";

    public MySpotifyPlayer(Player someSpotifyPlayer, Activity activeActivity) throws PlayerAlreadyExistsException {

        this.activeActivity = activeActivity;

        if (mySpotifyPlayer == null) {
            mySpotifyPlayer = someSpotifyPlayer;
            mySpotifyPlayer.addConnectionStateCallback(MySpotifyPlayer.this);
            mySpotifyPlayer.addNotificationCallback(MySpotifyPlayer.this);
        } else
            throw new PlayerAlreadyExistsException("Player already exists");
    }

    static Player getMySpotifyPlayer(){
        return mySpotifyPlayer;
    }

    static void setActiveActivity(Activity newActivity){
        activeActivity = newActivity;
    }

    static PlaybackState getPlaybackState(){
        return mySpotifyPlayer.getPlaybackState();
    }

    @Override
    public void onLoggedIn() {
        Toast.makeText(activeActivity, "Logged in", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut() {
        Toast.makeText(activeActivity, "Logged out", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(int i) {
        Toast.makeText(activeActivity, "Log in Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTemporaryError() {
        Toast.makeText(activeActivity, "Temporary Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionMessage(String s) {
        Toast.makeText(activeActivity, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Toast.makeText(activeActivity, "Playback event", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaybackError(Error error) {
        Toast.makeText(activeActivity, "Playback Error", Toast.LENGTH_LONG).show();
    }



    public class PlayerAlreadyExistsException extends Throwable {
        public PlayerAlreadyExistsException(String message){
            super(message);
        }
    }
}
