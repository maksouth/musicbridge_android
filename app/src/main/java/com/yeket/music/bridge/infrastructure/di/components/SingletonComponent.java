package com.yeket.music.bridge.infrastructure.di.components;

import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.di.modules.CacheChatUserDataModule;
import com.yeket.music.bridge.infrastructure.di.modules.FirebaseDataSourceModule;
import com.yeket.music.bridge.infrastructure.di.modules.SpotifyModule;
import com.yeket.music.bridge.infrastructure.di.modules.UserModule;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.services.ChatUserManager;
import com.yeket.music.bridge.services.SpotifyServiceHolder;

import javax.inject.Singleton;

import dagger.Component;
import kaaes.spotify.webapi.android.SpotifyService;

@Singleton
@Component( modules = {
        UserModule.class,
        CacheChatUserDataModule.class,
        SpotifyModule.class,
        FirebaseDataSourceModule.class
})
public interface SingletonComponent {

    User getChatUser();

    LuresUser getUser();

    ChatUserManager getChatUserManager();

    UserHolder getUserHolder();

    SpotifyService getSpotifyService();

    SpotifyServiceHolder getSpotifyServiceHolder();

}
