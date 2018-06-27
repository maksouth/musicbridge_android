package com.yeket.music.bridge.ui.views.app_settings;

import android.support.annotation.NonNull;
import android.view.View;

import com.yeket.music.bridge.datasource.settings.SettingsDataSource;
import com.yeket.music.bridge.models.LuresUser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserSettingsViewModel implements AppSettingsContract.ViewModel {

    private Boolean messageSwitch = Boolean.TRUE;
    private Boolean likeSwitch = Boolean.TRUE;
    private Boolean matchSwitch = Boolean.TRUE;

    private AppSettingsContract.View view;

    private SettingsDataSource settingsDataSource;

    private String userId;

    @Inject
    public UserSettingsViewModel(@NonNull AppSettingsContract.View view,
                                 @NonNull LuresUser user,
                                 @NonNull SettingsDataSource settingsDataSource){
        this.view = view;
        this.settingsDataSource = settingsDataSource;
        userId = user.getId();
    }

    public void loadValues(){
        settingsDataSource.isSubscribed(userId, SettingsDataSource.NotificationType.NEW_LIKE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((v) -> {
                    likeSwitch = v;
                    view.setNewLikeNotification(v);
                });

        settingsDataSource.isSubscribed(userId, SettingsDataSource.NotificationType.NEW_MESSAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((v)->{
                    matchSwitch = v;
                    view.setNewMessageNotification(v);
                });

        settingsDataSource.isSubscribed(userId, SettingsDataSource.NotificationType.MATCH)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((v) -> {
                    matchSwitch = v;
                    view.setNewMatchNotification(v);
                });
    }

    @Override
    public void notificationSwitched(SettingsDataSource.NotificationType type, boolean state) {
        switch (type){
            case NEW_LIKE: likeSwitch = state; break;
            case NEW_MESSAGE: messageSwitch = state; break;
            case MATCH: matchSwitch = state; break;
        }
    }

    @Override
    public void saveClicked(View v) {
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.NEW_MESSAGE, messageSwitch);
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.NEW_LIKE, likeSwitch);
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.MATCH, matchSwitch);
        view.goNextScreen();
    }
}
