package com.yeket.music.bridge.datasource.util;

/**
 * interface contains
 * names of node and fields
 * for different datasets in Firebase Realtime Database
 */
public interface Schemes {

    String SLASH = "/";

    interface Chat {
        String CHAT_NODE_NAME = "chat";

        String DIALOGS = "dialogs";
        String MEMBERS = "members";
        String LAST_MESSAGE = "last_message";
        String LAST_UPDATE_TIME = "last_update_time";

        String MESSAGES = "messages";
        String MESSAGE_ID = "id";
        String IS_READ = "is_read";
        String PAYLOAD = "payload";
        String SENDER_ID = "sender_id";
        String TIMESTAMP = "timestamp";

        String USERS = "users";
        String USER_ID = "id";
        String IMAGE_URL = "image_url";
        String NAME = "name";
    }

    interface Likes {
        String LIKES_NODE_NAME = "Likes";
    }

    interface Dislikes {
        String DISLIKES_NODE_NAME = "Dislikes";
    }

    interface Music {
        String TRACKS_NODE_NAME = "Tracks";
        String ARTISTS_NODE_NAME = "Artists";
        String GENRES_NODE_NAME = "genres";
        String GENRES_ALL = "all";
        String GENRES_USERS = "users";
        String TRACK_NAME = "name";
        String TRACK_DURATION_MS = "duration_ms";
        String TRACK_PREVIEW_URL = "preview_url";
        String TRACK_IMAGE_URL = "image_url";
        String ARTIST_IMAGE_URL = "imageUrl";
        String ARTIST_NAME = "name";
    }

    interface Settings {
        String SETTINGS_NODE_NAME = "settings";
        String NOTIFICATIONS_NODE_NAME = "notifications";
    }

    interface Tokens {
        String TOKENS_NODE_NAME = "Tokens";
    }

    interface Preferences {
        String PREFERENCES_NODE_NAME = "user_preferences";
        String MIN_AGE = "min";
        String MAX_AGE = "max";
        String AGE_RANGE = "age_range";
    }

    interface User {
        String USERS_NODE_NAME = "Users";
        String EMAIL = "email";
        String FACEBOOK_ID = "facebook_id";
        String SPOTIFY_ID = "spotify_id";
        String DISPLAY_NAME = "display_name";
        String IMAGE_URI = "image_uri";
        String GENDER = "gender";
        String BIRTHDATE = "birthdate";
        String LOCATION_LAT = "location_lat";
        String LOCATION_LON = "location_lon";
        String ABOUT_ME = "about_me";
        String TIMESTAMP = "timestamp";
    }

}
