package com.yeket.music.bridge.services;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.datasource.users.UsersDataSource;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.LuresUser;

import java.util.Date;

import javax.inject.Inject;

public class UserManagerImpl implements UserManager {


    private UsersDataSource usersDataSource;

    private UserHolder userHolder;

    @Inject
    public UserManagerImpl(UsersDataSource usersDataSource,
                           UserHolder userHolder){
        this.usersDataSource = usersDataSource;
        this.userHolder = userHolder;
    }

    @Override
    public void getUser(@NonNull String userId,
                             @NonNull SuccessCallback<LuresUser> callback,
                             @Nullable FailureCallback failureCallback){
        usersDataSource.getUser(userId,
                u -> {
                    userHolder.store(u);
                    callback.onSuccess(u);},
                failureCallback);
    }

    @Override
    public void createUser(@NonNull String id,
                           @NonNull String displayName,
                           @Nullable SuccessCallback<LuresUser> callback,
                           @Nullable FailureCallback failureCallback){

        usersDataSource.createUser(id,
                displayName,
                ()->{
                    LuresUser user = new LuresUser();
                    user.setId(id);
                    user.setDisplayName(displayName);
                    if (callback != null)
                        callback.onSuccess(user);
                },
                failureCallback);
    }

    @Override
    public void updateLocation(@NonNull Location location,
                               @Nullable CompleteCallback completeCallback,
                               @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setLocation(location);
        usersDataSource.<Double>changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.LOCATION_LAT,
                location.getLatitude(),
                () -> usersDataSource.<Double>changeUserProperty(userHolder.getUser().getId(),
                        Schemes.User.LOCATION_LON,
                        location.getLongitude(),
                        completeCallback,
                        failureCallback),
                failureCallback);
    }

    @Override
    public void setSpotifyId(@NonNull String spotifyId,
                             @Nullable CompleteCallback completeCallback,
                             @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setSpotifyId(spotifyId);
        usersDataSource.changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.SPOTIFY_ID,
                spotifyId,
                completeCallback,
                failureCallback);
    }

    @Override
    public void setUserImageUri(@NonNull String imageUri,
                                @Nullable CompleteCallback completeCallback,
                                @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setImageUri(imageUri);
        usersDataSource.changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.IMAGE_URI,
                imageUri,
                completeCallback,
                failureCallback);
    }

    @Override
    public void setBirthdate(@NonNull Date date,
                             @Nullable CompleteCallback completeCallback,
                             @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setBirthdate(date.getTime());
        usersDataSource.<Long>changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.BIRTHDATE,
                date.getTime(),
                completeCallback,
                failureCallback);
    }

    @Override
    public void setAboutMe(@NonNull String caption,
                           @Nullable CompleteCallback completeCallback,
                           @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setAboutYou(caption);
        usersDataSource.changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.ABOUT_ME,
                caption,
                completeCallback,
                failureCallback);
    }

    @Override
    public void setEmail(@NonNull String email,
                         @Nullable CompleteCallback completeCallback,
                         @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setEmail(email);
        usersDataSource.changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.EMAIL,
                email,
                completeCallback,
                failureCallback);
    }

    @Override
    public void setGender(@NonNull String gender,
                          @Nullable CompleteCallback completeCallback,
                          @Nullable FailureCallback failureCallback) {

        userHolder.getUser().setGender(gender);
        usersDataSource.changeUserProperty(userHolder.getUser().getId(),
                Schemes.User.GENDER,
                gender,
                completeCallback,
                failureCallback);
    }

}
