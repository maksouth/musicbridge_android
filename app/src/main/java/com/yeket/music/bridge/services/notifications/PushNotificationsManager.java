package com.yeket.music.bridge.services.notifications;

import android.content.Context;

import com.yeket.music.bridge.models.notification_commands.Command;

import java.util.Map;

public class PushNotificationsManager {

    private NotificationCommandCreator notificationCommandCreator;
    private NotificationComposer notificationComposer;

    public PushNotificationsManager(Context context){
        notificationCommandCreator = new NotificationCommandCreator(context);
        notificationComposer = new NotificationComposer(context);
    }

    public void apply(Map<String, String> params){
        Command command = notificationCommandCreator.extractParams(params);
        command.exec();
        //notificationComposer.show(command.getNotificationParams());

    }

}
