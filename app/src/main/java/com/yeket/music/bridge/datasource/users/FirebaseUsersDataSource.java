package com.yeket.music.bridge.datasource.users;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.util.Checker;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.models.error.ErrorResponse;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.datasource.util.Schemes.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class FirebaseUsersDataSource implements UsersDataSource{

    DatabaseReference mDatabaseUsers;

    @Inject
    public FirebaseUsersDataSource(){
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(User.USERS_NODE_NAME);
        mDatabaseUsers.keepSynced(true);
    }

    @Override
    public void getUser(@NonNull String id,
                        @NonNull SuccessCallback<LuresUser> callback,
                        @Nullable FailureCallback failureCallback) {

        mDatabaseUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    LuresUser luresUser = readUserFromDataSnapshot(dataSnapshot);
                    luresUser.setId(id);
                    callback.onSuccess(luresUser);
                } else {
                    if (failureCallback != null)
                        failureCallback.onFailure(Errors.LOAD_USER_NOT_EXISTS);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(failureCallback != null)
                    failureCallback.onFailure(Errors.LOAD_USER_CANCELLED);
            }
        });
    }

    @Override
    public void getAllUsers(@NonNull SuccessCallback<List<LuresUser>> callback,
                            @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        mDatabaseUsers.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<LuresUser> users = new ArrayList<>();

                LuresUser tempUser;
                Iterable<DataSnapshot> userDatasnapshotList = dataSnapshot.getChildren();

                for (DataSnapshot userSnapshot : userDatasnapshotList) {
                    tempUser = readUserFromDataSnapshot(userSnapshot);
                    users.add(tempUser);
                }

                if (users.size() > 0) {
                    callback.onSuccess(users);
                } else
                    finalFailureCallback.onFailure(Errors.LOAD_USERS_NOT_EXIST);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.LOAD_USERS_CANCELLED);
            }
        });
    }

    LuresUser readUserFromDataSnapshot(DataSnapshot dataSnapshot){

        LuresUser luresUser = new LuresUser();

        if (dataSnapshot.hasChild(User.DISPLAY_NAME)) {
            luresUser.setDisplayName((String) dataSnapshot.child(User.DISPLAY_NAME).getValue());
        }

        if (dataSnapshot.hasChild(User.FACEBOOK_ID)) {
            luresUser.setFacebookId((String) dataSnapshot.child(User.FACEBOOK_ID).getValue());
        }

        if (dataSnapshot.hasChild(User.IMAGE_URI)) {
            luresUser.setImageUri((String) dataSnapshot.child(User.IMAGE_URI).getValue());
        }

        if (dataSnapshot.hasChild(User.LOCATION_LAT)
                && dataSnapshot.hasChild(User.LOCATION_LAT)) {

            Location location = new Location("");
            double latitude = Checker.extract(dataSnapshot.child(User.LOCATION_LAT).getValue(), Default.Users.LOCATION_LAT);
            double longitude = Checker.extract(dataSnapshot.child(User.LOCATION_LON).getValue(), Default.Users.LOCATION_LON);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            luresUser.setLocation(location);

        }

        if (dataSnapshot.hasChild(User.ABOUT_ME)) {
            luresUser.setAboutYou((String) dataSnapshot.child(User.ABOUT_ME).getValue());
        }

        if (dataSnapshot.hasChild(User.SPOTIFY_ID)) {
            luresUser.setSpotifyId((String) dataSnapshot.child(User.SPOTIFY_ID).getValue());
        }

        if (dataSnapshot.hasChild(User.GENDER)) {
            luresUser.setGender((String) dataSnapshot.child(User.GENDER).getValue());
        }

        if (dataSnapshot.hasChild(User.BIRTHDATE)) {
            Long birthdateStr = Checker.extract(dataSnapshot.child(User.BIRTHDATE).getValue(), new Date().getTime());
            luresUser.setBirthdate(birthdateStr);
        }

        if (dataSnapshot.hasChild(User.EMAIL)) {
            luresUser.setEmail((String)dataSnapshot.child(User.EMAIL).getValue());
        }

        luresUser.setId(dataSnapshot.getKey());

        return luresUser;
    }

    @Override
    public void createUser(@NonNull String id,
                           @NonNull String displayName,
                           @Nullable CompleteCallback callback,
                           @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(id)){

                    finalFailureCallback.onFailure(Errors.CREATE_USER_ALREADY_EXISTS);

                } else {

                    mDatabaseUsers.child(id)
                                    .child(User.DISPLAY_NAME).setValue(displayName)
                                    .addOnCompleteListener(task -> {

                                        if (callback !=null)
                                            callback.onComplete();

                                    }).addOnFailureListener(e -> {
                                        // TODO: 03.01.18 add error reporting to all failure listeners, so exception would not be lost
                                        finalFailureCallback.onFailure(Errors.WRITE_USER_NAME_FAIL);

                                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                finalFailureCallback.onFailure(Errors.WRITE_USER_NAME_CANCELLED);

            }
        });
    }

    public <T> void changeUserProperty(@NonNull String userId,
                                       @NonNull String property,
                                       @NonNull T value,
                                       @Nullable CompleteCallback completeCallback,
                                       @Nullable FailureCallback failureCallback){

        if(value == null){
            if (failureCallback != null)
                failureCallback.onFailure(new ErrorResponse(Errors.UPDATE_PROPERTY_NULL_VALUE, property));
            return;
        }

        if(failureCallback == null) failureCallback = errorResponse -> {};
        if(completeCallback == null) completeCallback = () -> {};

        FailureCallback finalFailureCallback = failureCallback;
        CompleteCallback finalCompleteCallback = completeCallback;

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(TextUtils.isEmpty(userId)) {
                    finalFailureCallback.onFailure(Errors.LOAD_USER_NOT_EXISTS);
                } else {

                    if (dataSnapshot.hasChild(userId)) {

                        mDatabaseUsers.child(userId).child(property).setValue(value)
                                .addOnCompleteListener(task -> finalCompleteCallback.onComplete())
                                .addOnFailureListener(e -> finalFailureCallback.onFailure(
                                        new ErrorResponse(Errors.UPDATE_PROPERTY_FAIL, property, value.toString(), e.getLocalizedMessage())));

                    } else {
                        finalFailureCallback.onFailure(Errors.LOAD_USER_NOT_EXISTS);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.UPDATE_PROPERTY_CANCELLED);
            }
        });
    }

}
