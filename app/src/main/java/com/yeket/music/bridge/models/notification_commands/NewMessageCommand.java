package com.yeket.music.bridge.models.notification_commands;

import android.util.Log;

import com.yeket.music.R;
import com.yeket.music.bridge.services.notifications.constants.NotificationIds;

import java.util.Map;

public class NewMessageCommand implements Command {

    private static final String TAG = NewMessageCommand.class.getSimpleName();
    private static final String USER_ID = "id";

    private String fromWhoId;

    public NewMessageCommand(Map<String, String> params){
        fromWhoId = params.get(USER_ID);
    }

    @Override
    public void exec() {
        Log.d(TAG, "New message from " + fromWhoId);
    }

    @Override
    public NotificationParams getNotificationParams() {
        return new NotificationParams(R.string.title_new_message,
                                        R.string.text_new_message,
                                        R.drawable.ic_chat_bubble_white_24dp,
                                        NotificationIds.NEW_MESSAGE_NOTIFICATION_ID);
    }
}
