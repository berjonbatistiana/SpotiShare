package com.spotishare.frey.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by frey on 11/14/16.
 */

public class SpotiShareSharedPreferences {
    private final static String SPOTISHARE_PREFERENCE = "spotishare-pref";
    private final static String SPOTIFY_UID = "spotify-uid";

    public void setSpotifyProfileId(Context c, String uid){
        SharedPreferences.Editor spe = c.getSharedPreferences(SPOTISHARE_PREFERENCE, Context.MODE_PRIVATE).edit();
        spe.putString(SPOTIFY_UID, uid);
    }

    public String getSpotifyProfileId(Context c){
        SharedPreferences sp = c.getSharedPreferences(SPOTISHARE_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString(SPOTIFY_UID, "");
    }

}
