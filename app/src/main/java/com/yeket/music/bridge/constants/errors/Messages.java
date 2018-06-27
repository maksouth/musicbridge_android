package com.yeket.music.bridge.constants.errors;

public interface Messages {

    // message name pattern
    // ACTION_DATA_CONDITION
    // LOAD_USER_NOT_EXISTS
    // CREATE_USER_ALREADY_EXISTS

    // TODO: 03.01.18 see whether we need to add parameters to error messages

    String LOAD_USER_NOT_EXISTS = "Node with required user id not exists";
    String LOAD_USER_CANCELLED = "Cancelled while getting user";
    String CREATE_USER_FAILED = "Create user transaction failed. Database error: %s";
    String CREATE_USER_ALREADY_EXISTS = "Failed to create user with id on Firebase - already exists";

    String LOAD_USERS_NOT_EXISTS = "Node with all users does not exist";
    String LOAD_USERS_CANCELLED = "Cancelled while getting all users";

    String WRITE_USER_NAME_FAIL = "Failed to write user name to Firebase";
    String WRITE_USER_NAME_CANCELLED = "Cancelled while writing user name to Firebase";

    String LOAD_CHAT_USER_NOT_EXISTS = "Node with required chat user id not exists";
    String LOAD_CHAT_USER_CANCELLED = "Cancelled while getting chat user";

    String LOAD_MESSAGES_DIALOG_ID_NULL = "Provided dialog was null when requested to load messages";
    String LOAD_MESSAGES_NOT_EXISTS = "Older messages do not exist";
    String LOAD_MESSAGES_CANCELLED = "Cancelled white loading older messages";

    String CREATE_MESSAGE_FAILED = "Create message transaction failed";

    String GET_DIALOGS_CANCELLED = "Cancelled white getting dialogs for current user";
    String GET_COUPLE_DIALOG_NOT_EXISTS = "Dialog for given couple not exists. Ids: {%s, %s}";
    String GET_COUPLE_DIALOG_CANCELLED = "Cancelled while looking for couple dialog. Ids: {%s, %s}";

    String UPDATE_PROPERTY_NULL_VALUE = "Trying to update Firebase property \'%s\' with null value";
    String UPDATE_PROPERTY_FAILS = "Failed to update Firebase property \'%s\' with value \'%s\'. Reason: %s";
    String UPDATE_PROPERTY_CANCELLED = "Cancelled while updating Firebase property";

    String WRITE_IMAGE_FAIL = "Failed to write image url to Firebase";

    String GET_TRACKS_USER_ID_NULL = "User id was null when requested user tracks";
    String GET_TRACKS_NOT_FOUND = "No tracks found for provided user id";
    String GET_TRACKS_CANCELLED = "Cancelled while getting user tracks";

    String GET_TRACK_CANCELLED = "Cancelled while getting user favorite track";
    String GET_TRACK_USER_ID_NULL = "User id was null when requested user favorite track";
    String GET_TRACK_NOT_FOUND = "No track found for provided user id";

    String WRITE_TRACK_CANCELLED = "Cancelled while writing user track";
    String WRITE_TRACK_FAIL = "Failed to save user track. Database error: %s";
    String WRITE_TRACK_USER_ID_NULL = "User id was null when saving user track to Firebase";
    String WRITE_TRACK_NULL_VALUE = "Track was null when saving user track to Firebase";

    String GET_ARTISTS_USER_ID_NULL = "User id was null when requested user artists";
    String GET_ARTISTS_NOT_FOUND = "No artists found for provided user id";
    String GET_ARTISTS_CANCELLED = "Cancelled while getting user artists";

    String WRITE_ARTISTS_CANCELLED = "Cancelled while writing user artists";
    String WRITE_ARTISTS_FAIL = "Failed to save user artists. Database error: %s";
    String WRITE_ARTISTS_USER_ID_NULL = "User id was null when saving user artists to Firebase";

    String GET_GENRES_USER_ID_NULL = "User id was null when requested user genres";
    String GET_GENRES_NOT_FOUND = "No genres found for provided user id";
    String GET_GENRES_CANCELLED = "Cancelled while getting user genres";

    String GET_AGE_RANGE_FAIL_TO_PARSE = "Unable to parse data for preferred age range from Firebase";
    String GET_AGE_RANGE_NOT_FOUND = "No preferred age range found for provided user";
    String GET_AGE_RANGE_CANCELLED = "Cancelled while getting user preferred age range";

    String GET_USER_RECOMMENDATIONS_NOT_FOUND = "No recommended users found";

    String LOCATION_SETTINGS_NOT_APPLIED = "Location settings changes are not applied";
    String ENABLE_LOCATION = "Please enable location";
    String UNABLE_EXECUTE_LOCATION_INTENT = "PendingIntent unable to execute request: %s";
    String LOCATION_SETTINGS_INADEQUATE = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
    String USER_INTERACTION_CANCELLED = "User interaction was cancelled.";
    String LOCATION_PERMISSION_DENIED = "Location permission denied";

}
