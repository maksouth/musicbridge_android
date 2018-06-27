package com.yeket.music.bridge.datasource.tokens;

public interface TokensDataSource {

    void addFirebaseToken(String userId, String token);

    void addFirebaseToken(String userId);

    void removeFirebaseToken(String userId);

}
