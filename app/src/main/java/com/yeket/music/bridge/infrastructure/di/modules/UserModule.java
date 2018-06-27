package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.models.chat.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Singleton
    @Provides
    UserHolder provideUserHolder(){
        return new UserHolder();
    }

    @Provides
    LuresUser provideCurrentUser(UserHolder userHolder){
        return userHolder.getUser();
    }

    @Provides
    User provideChatUser(UserHolder userHolder){
        return userHolder.getChatUser();
    }

}
