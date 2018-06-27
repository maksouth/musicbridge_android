package com.yeket.music.bridge.datasource.settings;

import io.reactivex.Single;

public interface SettingsDataSource {

    enum NotificationType{
        NEW_MESSAGE, NEW_LIKE, MATCH
    }

    Single<Boolean> isSubscribed(String userId, NotificationType type);

    void updateSubscription(String userId, NotificationType type, boolean isSubscribed);

}
