package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.services.player.PlayerContract;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayerViewModule {

    private PlayerContract.View view;

    public PlayerViewModule(PlayerContract.View view){
        this.view = view;
    }

    public PlayerViewModule(){}

    @Provides
    PlayerContract.View provideView(){
        return view;
    }

}
