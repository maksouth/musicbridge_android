package com.yeket.music.bridge.ui.views;

import android.support.multidex.MultiDexApplication;

import com.yeket.music.bridge.infrastructure.di.components.DaggerSingletonComponent;
import com.yeket.music.bridge.infrastructure.di.components.SingletonComponent;

public class LuresApplication extends MultiDexApplication {

    private SingletonComponent singletonComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        singletonComponent = DaggerSingletonComponent.create();
    }

    public SingletonComponent getSingletonComponent(){
        return singletonComponent;
    }

}
