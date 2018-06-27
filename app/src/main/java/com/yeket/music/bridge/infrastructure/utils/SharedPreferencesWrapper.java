package com.yeket.music.bridge.infrastructure.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {

    private static final String SP_NAME = "lures_shared_preferences";

    public static final String SPOTIFY_ID = "spotify_id";
    public static final String SIGNED_IN = "signed_in";
    public static final String FIREBASE_INSTANCE_TOKEN = "firebase_instance_token";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharedPreferencesWrapper(Context appContext){
        sp = appContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void save(String property, String value){
        editor.putString(property, value).apply();
    }

    public void save(String property, Long value){
        editor.putLong(property, value).apply();
    }

    public void save(String property, Boolean value){
        editor.putBoolean(property, value).apply();
    }

    public String getString(String property){
        return sp.getString(property, null);
    }

    public Long getLong(String property){
        return sp.getLong(property, 0);
    }

    public Boolean getBoolean(String property){
        return sp.getBoolean(property, false);
    }

}
