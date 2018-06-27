package com.yeket.music.bridge.infrastructure.di.components;

import com.yeket.music.bridge.infrastructure.di.PerActivity;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ContextNeedManagersModule;
import com.yeket.music.bridge.infrastructure.di.modules.FirebaseDataSourceModule;
import com.yeket.music.bridge.services.LuresFirebaseInstanceIdService;

import dagger.Component;

@PerActivity
@Component(modules = {
        FirebaseDataSourceModule.class,
        ContextModule.class,
        ContextNeedManagersModule.class
}, dependencies = SingletonComponent.class)
public interface FirebaseInstanceIdServiceComponent {
    void inject(LuresFirebaseInstanceIdService service);
}
