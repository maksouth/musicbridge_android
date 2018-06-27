package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.services.ChatUserManager;
import com.yeket.music.bridge.services.ChatUserManagerImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CacheChatUserDataModule {

    @Binds
    @Singleton
    abstract ChatUserManager provideChatUserManager(ChatUserManagerImpl chatUserManager);
}
