package com.yeket.music.bridge.datasource.util;


import com.yeket.music.bridge.models.chat.User;

public interface Default {

    interface Chat {
        int UNREAD_MESSAGES_COUNT = 0;
        String MESSAGE_PAYLOAD = "unable to show message";
        Boolean IS_READ = true;
        String LAST_MESSAGE_ID = "last_message_id";
        String MESSAGE_ID = "message_id";

        String ADMIN_ID = "lures_admin_user";
        String ADMIN_NAME = "Lures Admin";
        String ADMIN_AVATAR = "https://firebasestorage.googleapis.com/v0/b/luresfriends.appspot.com/o/ic_launcher_round_compress.png?alt=media&token=77a18439-48f6-4a57-9963-c092a7cfdbe2";

        User ADMIN = new User(ADMIN_ID, ADMIN_NAME, ADMIN_AVATAR);

        String USER_ID = "default_user_id";
        String USER_NAME = "Empty User";
        String USER_AVATAR = "https://firebasestorage.googleapis.com/v0/b/luresfriends.appspot.com/o/girl_icon_normal.webp?alt=media&token=f42e5656-e709-44bb-8c1d-7d80d3d26401";

        User USER = new User(USER_ID, USER_NAME, USER_AVATAR);
    }

    interface Music {
        long TRACK_LENGTH_MS = 3 * 60 * 1000;
    }

    interface Preferences {
        int MIN_AGE = 18;
        int MAX_AGE = 24;
    }

    interface Users {
        double LOCATION_LAT = 0;
        double LOCATION_LON = 0;
    }

}
