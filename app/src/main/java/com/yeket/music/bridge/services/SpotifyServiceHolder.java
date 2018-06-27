package com.yeket.music.bridge.services;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceHolder {

    private SpotifyApi spotifyApi;
    private SpotifyService spotifyService;
    private String accessToken;

    public SpotifyServiceHolder(){
        Log.d(SpotifyServiceHolder.class.getSimpleName(), "create new " + this);
    }

    public SpotifyService getService(){
        return spotifyService;
    }

    public void initialize(String token){
        accessToken = token;

        spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(accessToken);

        spotifyService = spotifyApi.getService();

    }

    public String getAccessToken(){
        return accessToken;
    }
}
