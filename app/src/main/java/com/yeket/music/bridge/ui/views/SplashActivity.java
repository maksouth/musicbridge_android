package com.yeket.music.bridge.ui.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.yeket.music.bridge.ui.ui_components.TypefaceUtil;
import com.yeket.music.bridge.ui.views.login.LoginActivity;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private static boolean hasBeenCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        if(!hasBeenCreated){

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        hasBeenCreated = true;

        TypefaceUtil.overrideFont(getApplicationContext()); // font from assets: "assets/fonts/Roboto-Regular.ttf

        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
