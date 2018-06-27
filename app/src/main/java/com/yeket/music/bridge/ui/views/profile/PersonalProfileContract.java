package com.yeket.music.bridge.ui.views.profile;

import com.yeket.music.bridge.ui.views.BaseContract;

import java.util.List;

public interface PersonalProfileContract {

    interface View extends BaseContract.View{
        void setImage(String url);
        void setName(String name);
        void setAge(String age);
        void goToLoginScreen();
        void goEditGenresScreen(List<String> genres, List<String> favoriteGenres);
        void goToEditProfileScreen();
        void goEditAppSettingsScreen();
        void showGenres(List<String> genres);
    }

    interface Presenter{
        void editButtonClicked();
        void logoutButtonClicked();
        void editGenresButtonClicked();
        void editAppSettingsClicked();
        void start();
    }

}
