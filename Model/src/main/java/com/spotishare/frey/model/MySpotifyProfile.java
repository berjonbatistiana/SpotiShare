package com.spotishare.frey.model;

/**
 * Created by frey on 11/14/16.
 */

public class MySpotifyProfile extends SpotifyProfile {

    public static SpotifyProfile mySpotifyProfile;

    public MySpotifyProfile(String stringSpotifyProfile) {
        super(stringSpotifyProfile);
        mySpotifyProfile = this;
    }
}
