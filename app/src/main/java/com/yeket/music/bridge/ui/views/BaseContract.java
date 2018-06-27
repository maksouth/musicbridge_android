package com.yeket.music.bridge.ui.views;

import android.support.annotation.StringRes;

public interface BaseContract {

    interface View{
        void showError(@StringRes int messageResourceId);
        void showError(String message);
        void showProgressBar(@StringRes int messageResourceId);
        void dismissProgressDialog();
    }
}
