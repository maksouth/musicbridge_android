package com.yeket.music.bridge.ui.views.login;

import android.content.Intent;

import com.yeket.music.bridge.ui.views.BaseContract;

public interface LoginContract {

    int REQUEST_CODE = 1337;

    interface View extends BaseContract.View{
        void goToMainActivity();
        void goToDetailsActivity();
        void showSpotifyLogin(boolean showDialog);
        void setExplicitLoginView();
        void showError(String message);
    }

    interface Presenter{
        void start();
        void loginSpotify();
        void fetchSpotifyLoginResults(int requestCode, int resultCode, Intent intent);
    }

}
