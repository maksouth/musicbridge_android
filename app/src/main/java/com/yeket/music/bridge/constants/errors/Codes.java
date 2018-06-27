package com.yeket.music.bridge.constants.errors;

/**
 * Domains:
 * 10 - Firebase Realtime Database
 * 20 - Spotify SDK
 * 30 - Network
 * 40 - recommendations
 * 50 - location
 */
public interface Codes {

    String NOT_EXISTS = "1001";
    String CANCELLED = "1002";
    String DIALOG_ID_NULL = "1003";
    String USER_ID_NULL = "1004";
    String CREATE_FAIL = "1005";
    String NULL_VALUE = "1006";
    String UPDATE_FAIL = "1007";
    String CREATE_ALREADY_EXIST = "1008";
    String WRITE_FAIL = "1009";
    String WRITE_NULL = "1010";
    String READ_PARSE_FAIL = "1011";

    String NO_RECOMMENDATIONS = "4001";

    String LOCATION_SETTINGS_NOT_APPLIED = "5001";
    String LOCATION_NOT_ENABLED = "5002";
    String UNABLE_EXECUTE_INTENT = "5003";
    String LOCATION_SETTINGS_INADEQUATE = "5004";
    String USER_INTERACTION_CANCELLED = "5005";
    String LOCATION_PERMISSION_DENIED = "5006";

}
