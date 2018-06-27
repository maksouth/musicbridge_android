package com.yeket.music.bridge.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yeket.music.bridge.services.notifications.PushNotificationsManager;

public class LuresFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = LuresFirebaseMessagingService.class.getSimpleName();
    public static final String ON_MESSAGE_RECEIVED_EVENT = "on-message-received";

    private PushNotificationsManager pushNotificationsManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            if(pushNotificationsManager == null){
                pushNotificationsManager = new PushNotificationsManager(getApplicationContext());
            }

            pushNotificationsManager.apply(remoteMessage.getData());
        }
    }

//
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
//    private void sendNotification(String messageBody) {
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle("FCM Message")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

}
