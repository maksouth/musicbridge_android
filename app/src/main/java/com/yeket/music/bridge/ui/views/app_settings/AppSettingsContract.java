package com.yeket.music.bridge.ui.views.app_settings;

import com.yeket.music.bridge.datasource.settings.SettingsDataSource;

public interface AppSettingsContract {

    interface View{
        void goNextScreen();
        void setNewMessageNotification(boolean value);
        void setNewLikeNotification(boolean value);
        void setNewMatchNotification(boolean value);

    }

    interface ViewModel {

        void loadValues();

        void notificationSwitched(SettingsDataSource.NotificationType type, boolean state);

        void saveClicked(android.view.View view);

    }
}
