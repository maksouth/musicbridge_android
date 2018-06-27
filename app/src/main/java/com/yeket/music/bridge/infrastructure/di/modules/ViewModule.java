package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.infrastructure.di.PerActivity;
import com.yeket.music.bridge.ui.views.app_settings.AppSettingsContract;
import com.yeket.music.bridge.ui.views.details.DetailsContract;
import com.yeket.music.bridge.ui.views.dialogs.DialogListContract;
import com.yeket.music.bridge.ui.views.home.HomeContract;
import com.yeket.music.bridge.ui.views.login.LoginContract;
import com.yeket.music.bridge.ui.views.main.MainContract;
import com.yeket.music.bridge.ui.views.messages.MessagesContract;
import com.yeket.music.bridge.ui.views.profile.PersonalProfileContract;
import com.yeket.music.bridge.ui.views.select_genres.ChooseGenresContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    private AppSettingsContract.View appSettingsView;
    private DetailsContract.View detailsView;
    private DialogListContract.View dialogListView;
    private HomeContract.View homeView;
    private LoginContract.View loginView;
    private MainContract.View mainView;
    private MessagesContract.View messagesView;
    private PersonalProfileContract.View personalProfileView;
    private ChooseGenresContract.View choseGenresView;

    public ViewModule(AppSettingsContract.View appSettingsView){
        this.appSettingsView = appSettingsView;
    }

    public ViewModule(DetailsContract.View detailsView){
        this.detailsView = detailsView;
    }

    public ViewModule(DialogListContract.View dialogListView){
        this.dialogListView = dialogListView;
    }

    public ViewModule(HomeContract.View view){
        this.homeView = view;
    }

    public ViewModule(LoginContract.View view){
        this.loginView = view;
    }

    public ViewModule(MainContract.View view){
        this.mainView = view;
    }

    public ViewModule(MessagesContract.View view){
        this.messagesView = view;
    }

    public ViewModule(PersonalProfileContract.View view){
        this.personalProfileView = view;
    }

    public ViewModule(ChooseGenresContract.View view){
        this.choseGenresView = view;
    }

    @Provides
    @PerActivity
    public AppSettingsContract.View provideAppSettingsView(){ return appSettingsView;}

    @Provides
    @PerActivity
    public DetailsContract.View provideDetailsView(){ return detailsView;}

    @Provides
    @PerActivity
    public DialogListContract.View provideDialogListView(){ return dialogListView;}

    @Provides
    @PerActivity
    public HomeContract.View provideHomeView(){ return homeView;}

    @Provides
    @PerActivity
    public LoginContract.View provideLoginView(){ return loginView;}

    @Provides
    @PerActivity
    public MainContract.View provideMainView(){ return mainView;}

    @Provides
    @PerActivity
    public MessagesContract.View provideMessagesView(){ return messagesView;}

    @Provides
    @PerActivity
    public PersonalProfileContract.View providePersonalProfileView(){ return personalProfileView;}

    @Provides
    @PerActivity
    public ChooseGenresContract.View provideChooseGenresView(){ return choseGenresView;}
}
