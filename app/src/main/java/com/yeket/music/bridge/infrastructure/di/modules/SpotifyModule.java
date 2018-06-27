package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.services.SpotifyServiceHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kaaes.spotify.webapi.android.SpotifyService;

@Module
public class SpotifyModule {

    @Provides
    public SpotifyService provideSpotifyService(SpotifyServiceHolder holder){
        return holder.getService();
    }

    @Singleton
    @Provides
    public SpotifyServiceHolder provideSpotifyServiceHolder(){
        return new SpotifyServiceHolder();
    }

}
