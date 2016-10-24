package com.spotishare.frey.controller.spotify;

import com.batistiana.bj.httprequestbj.HttpRequest;

/**
 * Created by frey on 10/24/16.
 */

public class SpotifyWebAPI {

    private String OAuth = "";
    private static final String SpotifyURI = "https://api.spotify.com";

    public SpotifyWebAPI (String OAuth){
        this.OAuth = "Bearer " + OAuth;
    }

    public void getMySpotifyProfile(){
        final HttpRequest request = new HttpRequest(SpotifyURI + "/v1/me", HttpRequest.GET);
        request.putRequestHeader("Authorization", OAuth);

        request.setPostExecute(new HttpRequest.AsyncPostExecuteInterface() {
            @Override
            public void OnPostExecute() {
                request.getResponse();
            }
        });

        request.execute();
    }
}
