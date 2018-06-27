package com.yeket.music.bridge.infrastructure.di.components;

import com.yeket.music.bridge.infrastructure.di.ComponentScope;
import com.yeket.music.bridge.infrastructure.di.modules.FirebaseDataSourceModule;
import com.yeket.music.bridge.infrastructure.di.modules.ServicesModule;
import com.yeket.music.bridge.ui.views.match_dialog.MatchFullScreenDialog;

import dagger.Component;

@ComponentScope
@Component(modules = {
        FirebaseDataSourceModule.class,
        ServicesModule.class
}, dependencies = SingletonComponent.class)
public interface MatchFullScreenDialogComponent {
    void inject(MatchFullScreenDialog matchFullScreenDialog);
}
