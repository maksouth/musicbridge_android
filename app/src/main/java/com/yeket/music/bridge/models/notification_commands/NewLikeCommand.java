package com.yeket.music.bridge.models.notification_commands;

import android.util.Log;

import com.yeket.music.R;
import com.yeket.music.bridge.services.notifications.constants.NotificationIds;

import java.util.Map;

public class NewLikeCommand implements Command {

    private static final String TAG = NewLikeCommand.class.getSimpleName();
    private static final String USER_ID = "id";

    private String whoLikedId;

    public NewLikeCommand(Map<String, String> params){
        whoLikedId = params.get(USER_ID);
    }

    @Override
    public void exec() {
        Log.d(TAG, "New like from " + whoLikedId);
    }

    @Override
    public NotificationParams getNotificationParams() {
        return new NotificationParams(R.string.title_new_like,
                                        R.string.text_new_like,
                                        R.drawable.ic_favorite_white_24dp,
                                        NotificationIds.NEW_LIKE_NOTIFICATION_ID);
    }

}
