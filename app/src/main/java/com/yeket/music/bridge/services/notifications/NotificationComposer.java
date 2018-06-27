package com.yeket.music.bridge.services.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.yeket.music.bridge.models.notification_commands.NotificationParams;
import com.yeket.music.bridge.ui.views.SplashActivity;

class NotificationComposer {

    private Context context;
    private NotificationManager notificationManager;

    public NotificationComposer(Context context){
        this.context = context;
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    void show(NotificationParams params){

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(params.getTitleResourceId()))
                .setContentText(context.getString(params.getTextResourceId()))
                .setSmallIcon(params.getSmallIconResourceId())
                .setSound(defaultSoundUri);

        Intent resultIntent = new Intent(context, SplashActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(params.getNotificationId(), notificationBuilder.build());

    }

}
