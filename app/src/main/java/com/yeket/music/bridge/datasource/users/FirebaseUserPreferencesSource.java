package com.yeket.music.bridge.datasource.users;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.util.Checker;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.models.AgeRange;
import com.yeket.music.bridge.models.error.ErrorResponse;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.datasource.util.Schemes.Preferences;

import javax.inject.Inject;

public class FirebaseUserPreferencesSource implements UserPreferencesSource {

    private DatabaseReference mDatabaseUserPreferences;

    @Inject
    public FirebaseUserPreferencesSource(){

        mDatabaseUserPreferences = FirebaseDatabase.getInstance().getReference().child(Preferences.PREFERENCES_NODE_NAME);
        mDatabaseUserPreferences.keepSynced(true);

    }

    @Override
    public void setMinAgeRange(@NonNull String userId,
                               int value,
                               @Nullable CompleteCallback callback,
                               @Nullable FailureCallback failureCallback) {
        changeUserProperty(userId,
                Preferences.AGE_RANGE + Schemes.SLASH + Preferences.MIN_AGE,
                String.valueOf(value),
                callback, failureCallback);
    }

    @Override
    public void setMaxAgeRange(@NonNull String userId,
                               int value,
                               @Nullable CompleteCallback callback,
                               @Nullable FailureCallback failureCallback) {
        changeUserProperty(userId, Preferences.AGE_RANGE + Schemes.SLASH + Preferences.MAX_AGE, String.valueOf(value), callback, failureCallback);
    }

    @Override
    public void getAgeRange(@NonNull String userId,
                            @NonNull SuccessCallback<AgeRange> callback,
                            @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        mDatabaseUserPreferences.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.child(Preferences.AGE_RANGE).exists()){
                    AgeRange ageRange = new AgeRange();
                    Object minAgeObject = dataSnapshot.child(Preferences.AGE_RANGE).child(Preferences.MIN_AGE).getValue();
                    Object maxAgeObject = dataSnapshot.child(Preferences.AGE_RANGE).child(Preferences.MAX_AGE).getValue();
                    int minAge = Checker.extract(minAgeObject, Default.Preferences.MIN_AGE);
                    int maxAge = Checker.extract(maxAgeObject, Default.Preferences.MAX_AGE);

                    try{
                        ageRange.setMinAge(minAge);
                        ageRange.setMaxAge(maxAge);
                        callback.onSuccess(ageRange);
                    } catch (IllegalArgumentException e){
                        finalFailureCallback.onFailure(Errors.GET_AGE_RANGE_FAIL_TO_PARSE);
                    }
                } else {
                    finalFailureCallback.onFailure(Errors.GET_AGE_RANGE_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.GET_AGE_RANGE_CANCELLED);
            }
        });

    }

    private void changeUserProperty(@NonNull String userId,
                                    @NonNull String property,
                                    @NonNull String value,
                                    @Nullable CompleteCallback callback,
                                    @Nullable FailureCallback failureCallback){

        CompleteCallback finalCompleteCallback = CompleteCallback.notNull(callback);
        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        if(value == null){
            finalFailureCallback.onFailure(new ErrorResponse(Errors.UPDATE_PROPERTY_NULL_VALUE, property));
            return;
        }

        mDatabaseUserPreferences.child(userId)
                                .child(property)
                                .setValue(value)
                                .addOnSuccessListener(aVoid -> finalCompleteCallback.onComplete())
                                .addOnFailureListener(e -> finalFailureCallback.onFailure(new ErrorResponse(Errors.UPDATE_PROPERTY_FAIL, property, value, e.getMessage())));
    }
}
