package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.datasource.users.UsersDataSource;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.services.ChatUserManager;
import com.yeket.music.bridge.services.ChatUserManagerImpl;
import com.yeket.music.bridge.services.UserManager;
import com.yeket.music.bridge.services.UserManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServicesModule {

    @Provides
    UserManager provideUserManager(UsersDataSource usersDataSource, UserHolder userHolder){
        return new UserManagerImpl(usersDataSource, userHolder);
    }

}
