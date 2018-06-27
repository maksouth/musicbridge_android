package com.yeket.music.bridge.models.notification_commands;

public interface Command {
    void exec();
    NotificationParams getNotificationParams();
}
