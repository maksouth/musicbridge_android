package com.yeket.music.bridge.services.notifications;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.yeket.music.bridge.models.notification_commands.Command;
import com.yeket.music.bridge.models.notification_commands.MutualLikeCommand;
import com.yeket.music.bridge.models.notification_commands.NewLikeCommand;
import com.yeket.music.bridge.models.notification_commands.NewMessageCommand;

import java.util.Map;


class NotificationCommandCreator {

    private static final String TYPE = "type";
    private static final String TYPE_MUTUAL_LIKE = "mutual_like";
    private static final String TYPE_LIKE = "new_like";
    private static final String TYPE_NEW_MESSAGE = "new_message";

    private Context context;

    public NotificationCommandCreator(Context context){
        this.context = context;
    }

    public Command extractParams(Map<String, String> params){
        String type = params.get(TYPE);
        Command command = null;

        switch (type){
            case TYPE_MUTUAL_LIKE:
                command = new MutualLikeCommand(params,
                                                getLocalBroadcastManager());
                break;
            case TYPE_LIKE:
                command = new NewLikeCommand(params);
                break;
            case TYPE_NEW_MESSAGE:
                command = new NewMessageCommand(params);
                break;
        }

        return command;
    }

    private LocalBroadcastManager getLocalBroadcastManager(){
        return LocalBroadcastManager.getInstance(context);
    }

}
