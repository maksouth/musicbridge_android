package com.yeket.music.bridge.infrastructure.di.modules;

import android.app.Activity;
import android.content.Context;

import com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper;
import com.yeket.music.bridge.services.LocationService;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextNeedManagersModule {

    @Provides
    public SharedPreferencesWrapper provide(Context context){
        return new SharedPreferencesWrapper(context);
    }

    @Provides
    public LocationService proovideLocationService(Activity context){
        return new LocationService(context);
    }

}
