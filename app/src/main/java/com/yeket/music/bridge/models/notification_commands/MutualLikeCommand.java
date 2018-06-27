package com.yeket.music.bridge.models.notification_commands;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.yeket.music.R;
import com.yeket.music.bridge.services.notifications.constants.NotificationIds;

import java.util.Map;

import static com.yeket.music.bridge.services.LuresFirebaseMessagingService.ON_MESSAGE_RECEIVED_EVENT;
import static com.yeket.music.bridge.services.notifications.constants.NotificationConstants.USER_1;
import static com.yeket.music.bridge.services.notifications.constants.NotificationConstants.USER_2;

public class MutualLikeCommand implements Command{

    private static final String TAG = MutualLikeCommand.class.getSimpleName();
    private static final String USER_ID_1 = "user_id_1";
    private static final String USER_ID_2 = "user_id_2";

    private String firstUserId;
    private String secondUserId;
    private LocalBroadcastManager localBroadcastManager;

    public MutualLikeCommand(Map<String, String> params,
                             LocalBroadcastManager localBroadcastManager){
        firstUserId = params.get(USER_ID_1);
        secondUserId = params.get(USER_ID_2);
        this.localBroadcastManager = localBroadcastManager;
    }

    public void exec(){
        Log.d(TAG, "New mutual like " + firstUserId + " and " + secondUserId);

        Intent intent = new Intent(ON_MESSAGE_RECEIVED_EVENT);
        intent.putExtra(USER_1, firstUserId);
        intent.putExtra(USER_2, secondUserId);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public NotificationParams getNotificationParams() {
        return new NotificationParams(R.string.title_mutual_like,
                                        R.string.text_mutual_like,
                                        R.drawable.ic_favorite_white_24dp,
                                        NotificationIds.MUTUAL_LIKE_NOTIFICATION_ID);
    }

}
