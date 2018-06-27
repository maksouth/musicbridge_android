package com.yeket.music.bridge.datasource.tokens;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper;
import com.yeket.music.bridge.datasource.util.Schemes.Tokens;

import javax.inject.Inject;

public class FirebaseTokensDataSource implements TokensDataSource{

    private SharedPreferencesWrapper spManager;
    private DatabaseReference mDatabaseTokens;

    @Inject
    public FirebaseTokensDataSource(SharedPreferencesWrapper manager){
        spManager = manager;
        mDatabaseTokens = FirebaseDatabase.getInstance().getReference().child(Tokens.TOKENS_NODE_NAME);
        mDatabaseTokens.keepSynced(true);
    }

    /**
     * read firebase token from shared preferences
     * and store it to firebase database
     * @param userId
     */
    public void addFirebaseToken(String userId){
        String firebaseInstanceToken = spManager.getString(SharedPreferencesWrapper.FIREBASE_INSTANCE_TOKEN);
        if(firebaseInstanceToken!=null && userId != null) {
            mDatabaseTokens.child(userId).child(firebaseInstanceToken).setValue(true);
        }
    }

    @Override
    public void addFirebaseToken(String userId, String token) {

        spManager.save(SharedPreferencesWrapper.FIREBASE_INSTANCE_TOKEN, token);

        if(userId!=null && token != null) {
            mDatabaseTokens.child(userId).child(token).setValue(true);
        }
    }

    @Override
    public void removeFirebaseToken(String userId) {

        String token = spManager.getString(SharedPreferencesWrapper.FIREBASE_INSTANCE_TOKEN);

        if ( token != null && userId != null ) mDatabaseTokens.child(userId).child(token).removeValue();
    }

}
