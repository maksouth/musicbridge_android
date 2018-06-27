package com.yeket.music.bridge.models.notification_commands;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class NotificationParams {

    @StringRes
    private
    int titleResourceId;
    @StringRes
    private
    int textResourceId;
    @DrawableRes
    private
    int smallIconRes;

    private int notificationId;

    public NotificationParams(@StringRes int titleResourceId,
                              @StringRes int textResourceId,
                              @DrawableRes int smallIconRes,
                              int notificationId){
        this.titleResourceId = titleResourceId;
        this.textResourceId = textResourceId;
        this.smallIconRes = smallIconRes;
        this.notificationId = notificationId;
    }

    public int getTitleResourceId(){
        return titleResourceId;
    }

    public int getTextResourceId(){
        return textResourceId;
    }

    public int getSmallIconResourceId(){
        return smallIconRes;
    }

    public int getNotificationId(){
        return notificationId;
    }

}
