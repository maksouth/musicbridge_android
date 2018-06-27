package com.yeket.music.bridge.services;


import com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper;

import javax.inject.Inject;

import static com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper.SIGNED_IN;
import static com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper.SPOTIFY_ID;

// TODO: 21.09.17 remove this
public class AuthManager {

    private SharedPreferencesWrapper spManager;

    @Inject
    public AuthManager(SharedPreferencesWrapper manager) {
        spManager = manager;
    }

    public void loginWithSpotify(String spotifyId){
        spManager.save(SPOTIFY_ID, spotifyId);
        spManager.save(SIGNED_IN, true);
    }

    public boolean isSignedIn(){
        return spManager.getBoolean(SIGNED_IN);
    }

    public void logout(){
        spManager.save(SIGNED_IN, false);
    }

}
