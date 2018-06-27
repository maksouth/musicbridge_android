package com.yeket.music.bridge.infrastructure.di.components;

import com.yeket.music.bridge.infrastructure.di.PerActivity;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ContextNeedManagersModule;
import com.yeket.music.bridge.infrastructure.di.modules.FirebaseDataSourceModule;
import com.yeket.music.bridge.infrastructure.di.modules.PlayerViewModule;
import com.yeket.music.bridge.infrastructure.di.modules.ServicesModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.adapters.UserSwipeableCardAdapter;
import com.yeket.music.bridge.ui.views.app_settings.UserSettingsViewModel;
import com.yeket.music.bridge.ui.views.details.DetailsPresenter;
import com.yeket.music.bridge.ui.views.dialogs.DialogListPresenter;
import com.yeket.music.bridge.ui.views.home.HomePresenter;
import com.yeket.music.bridge.ui.views.login.LoginPresenter;
import com.yeket.music.bridge.ui.views.main.MainPresenter;
import com.yeket.music.bridge.ui.views.profile.PersonalProfilePresenter;
import com.yeket.music.bridge.ui.views.select_genres.ChooseGenresPresenter;

import dagger.Component;

@PerActivity
@Component(modules = {
        ContextModule.class,
        ContextNeedManagersModule.class,
        FirebaseDataSourceModule.class,
        ServicesModule.class,
        ViewModule.class,
        PlayerViewModule.class
}, dependencies = SingletonComponent.class)
public interface PresenterComponent {

    // TODO: 10.01.18 check whether we need context module everywhere we declare it
    // TODO: 10.01.18 search for todos in classes where dagger was not applied yet

    UserSettingsViewModel userSettingsViewModel();

    DetailsPresenter detailsPresenter();

    DialogListPresenter dialogListPresenter();

    HomePresenter homePresenter();

    LoginPresenter loginPresenter();

    MainPresenter mainPresenter();

    PersonalProfilePresenter personalProfilePresenter();

    ChooseGenresPresenter chooseGenresPresenter();

    UserSwipeableCardAdapter cardAdapter();

}
