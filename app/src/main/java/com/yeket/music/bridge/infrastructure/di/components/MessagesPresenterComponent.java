package com.yeket.music.bridge.infrastructure.di.components;

import com.yeket.music.bridge.infrastructure.di.ComponentScope;
import com.yeket.music.bridge.infrastructure.di.PerActivity;
import com.yeket.music.bridge.infrastructure.di.modules.DialogIdModule;
import com.yeket.music.bridge.infrastructure.di.modules.FirebaseDataSourceModule;
import com.yeket.music.bridge.infrastructure.di.modules.ServicesModule;
import com.yeket.music.bridge.infrastructure.di.modules.UserModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.views.messages.MessagesPresenter;

import javax.inject.Singleton;

import dagger.Component;

@PerActivity
@Component(modules = {
        ViewModule.class,
        DialogIdModule.class,
        FirebaseDataSourceModule.class,
        ServicesModule.class
}, dependencies = SingletonComponent.class)
public interface MessagesPresenterComponent {
    MessagesPresenter getMessagesPresenter();
}
