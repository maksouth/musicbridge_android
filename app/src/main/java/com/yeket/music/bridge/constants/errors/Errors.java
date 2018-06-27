package com.yeket.music.bridge.constants.errors;

import com.yeket.music.bridge.models.error.ErrorResponse;

public interface Errors {

    // error name pattern
    // action_data_condition
    // LOAD_USER_NOT_FOUND
    // UPDATE_PROPERTY_CANCELLED

    //user
    ErrorResponse CREATE_USER_FAIL = new ErrorResponse(Codes.CREATE_FAIL, Messages.CREATE_USER_FAILED);
    ErrorResponse LOAD_USER_NOT_EXISTS = new ErrorResponse(Codes.NOT_EXISTS, Messages.LOAD_USER_NOT_EXISTS);
    ErrorResponse CREATE_USER_ALREADY_EXISTS = new ErrorResponse(Codes.CREATE_ALREADY_EXIST, Messages.CREATE_USER_ALREADY_EXISTS);
    ErrorResponse LOAD_USER_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.LOAD_USER_CANCELLED);

    ErrorResponse WRITE_USER_NAME_FAIL = new ErrorResponse(Codes.WRITE_FAIL, Messages.WRITE_USER_NAME_FAIL);
    ErrorResponse WRITE_USER_NAME_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.WRITE_USER_NAME_CANCELLED);

    ErrorResponse LOAD_USERS_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.LOAD_USERS_NOT_EXISTS);
    ErrorResponse LOAD_USERS_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.LOAD_USERS_CANCELLED);

    //chat user
    ErrorResponse LOAD_CHAT_USER_NOT_EXISTS = new ErrorResponse(Codes.NOT_EXISTS, Messages.LOAD_CHAT_USER_NOT_EXISTS);
    ErrorResponse LOAD_CHAT_USER_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.LOAD_CHAT_USER_CANCELLED);

    //messages
    ErrorResponse LOAD_MESSAGES_DIALOG_ID_NULL = new ErrorResponse(Codes.DIALOG_ID_NULL, Messages.LOAD_MESSAGES_DIALOG_ID_NULL);
    ErrorResponse CREATE_MESSAGE_FAIL = new ErrorResponse(Codes.CREATE_FAIL, Messages.CREATE_MESSAGE_FAILED);

    ErrorResponse LOAD_MESSAGES_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.LOAD_MESSAGES_NOT_EXISTS);
    ErrorResponse LOAD_MESSAGES_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.LOAD_MESSAGES_CANCELLED);

    ErrorResponse GET_DIALOGS_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_DIALOGS_CANCELLED);
    ErrorResponse GET_COUPLE_DIALOG_NOT_EXISTS = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_COUPLE_DIALOG_NOT_EXISTS);
    ErrorResponse GET_COUPLE_DIALOG_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_COUPLE_DIALOG_CANCELLED);

    //general properties
    ErrorResponse UPDATE_PROPERTY_NULL_VALUE = new ErrorResponse(Codes.NULL_VALUE, Messages.UPDATE_PROPERTY_NULL_VALUE);
    ErrorResponse UPDATE_PROPERTY_FAIL = new ErrorResponse(Codes.UPDATE_FAIL, Messages.UPDATE_PROPERTY_FAILS);
    ErrorResponse UPDATE_PROPERTY_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.UPDATE_PROPERTY_CANCELLED);

    ErrorResponse WRITE_IMAGE_FAIL = new ErrorResponse(Codes.WRITE_FAIL, Messages.WRITE_IMAGE_FAIL);

    ErrorResponse WRITE_TRACK_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.WRITE_TRACK_USER_ID_NULL);
    ErrorResponse WRITE_TRACK_NULL_VALUE = new ErrorResponse(Codes.WRITE_NULL, Messages.WRITE_TRACK_NULL_VALUE);
    ErrorResponse WRITE_TRACK_FAIL = new ErrorResponse(Codes.WRITE_FAIL, Messages.WRITE_TRACK_FAIL);
    ErrorResponse WRITE_TRACK_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.WRITE_TRACK_CANCELLED);

    ErrorResponse GET_TRACKS_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.GET_TRACKS_USER_ID_NULL);
    ErrorResponse GET_TRACKS_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_TRACKS_NOT_FOUND);
    ErrorResponse GET_TRACKS_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_TRACKS_CANCELLED);

    ErrorResponse GET_TRACK_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.GET_TRACK_USER_ID_NULL);
    ErrorResponse GET_TRACK_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_TRACK_NOT_FOUND);
    ErrorResponse GET_TRACK_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_TRACK_CANCELLED);

    ErrorResponse GET_ARTISTS_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.GET_ARTISTS_USER_ID_NULL);
    ErrorResponse GET_ARTISTS_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_ARTISTS_NOT_FOUND);
    ErrorResponse GET_ARTISTS_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_ARTISTS_CANCELLED);

    ErrorResponse GET_GENRES_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.GET_GENRES_USER_ID_NULL);
    ErrorResponse GET_GENRES_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_GENRES_NOT_FOUND);
    ErrorResponse GET_GENRES_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_GENRES_CANCELLED);

    ErrorResponse WRITE_ARTISTS_USER_ID_NULL = new ErrorResponse(Codes.USER_ID_NULL, Messages.WRITE_ARTISTS_USER_ID_NULL);
    ErrorResponse WRITE_ARTISTS_FAIL = new ErrorResponse(Codes.WRITE_FAIL, Messages.WRITE_ARTISTS_FAIL);
    ErrorResponse WRITE_ARTISTS_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.WRITE_ARTISTS_CANCELLED);

    ErrorResponse GET_AGE_RANGE_FAIL_TO_PARSE = new ErrorResponse(Codes.READ_PARSE_FAIL, Messages.GET_AGE_RANGE_FAIL_TO_PARSE);
    ErrorResponse GET_AGE_RANGE_NOT_EXIST = new ErrorResponse(Codes.NOT_EXISTS, Messages.GET_AGE_RANGE_NOT_FOUND);
    ErrorResponse GET_AGE_RANGE_CANCELLED = new ErrorResponse(Codes.CANCELLED, Messages.GET_AGE_RANGE_CANCELLED);

    ErrorResponse GET_RECOMMENDATIONS_NOT_FOUND = new ErrorResponse(Codes.NO_RECOMMENDATIONS, Messages.GET_USER_RECOMMENDATIONS_NOT_FOUND);

    ErrorResponse LOCATION_SETTING_NOT_APPLIED = new ErrorResponse(Codes.LOCATION_SETTINGS_NOT_APPLIED, Messages.LOCATION_SETTINGS_NOT_APPLIED);
    ErrorResponse LOCATION_NOT_ENABLED = new ErrorResponse(Codes.LOCATION_NOT_ENABLED, Messages.ENABLE_LOCATION);
    ErrorResponse LOCATION_INTENT_UNABLE_TO_EXECUTE = new ErrorResponse(Codes.UNABLE_EXECUTE_INTENT, Messages.UNABLE_EXECUTE_LOCATION_INTENT);
    ErrorResponse LOCATION_SETTINGS_INADEQUATE = new ErrorResponse(Codes.LOCATION_SETTINGS_INADEQUATE, Messages.LOCATION_SETTINGS_INADEQUATE);
    ErrorResponse USER_INTERACTION_CANCELLED = new ErrorResponse(Codes.USER_INTERACTION_CANCELLED, Messages.USER_INTERACTION_CANCELLED);
    ErrorResponse LOCATION_PERMISSION_DENIED = new ErrorResponse(Codes.LOCATION_PERMISSION_DENIED, Messages.LOCATION_PERMISSION_DENIED);

}
