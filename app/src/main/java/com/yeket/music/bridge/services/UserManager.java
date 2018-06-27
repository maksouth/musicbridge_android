package com.yeket.music.bridge.services;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.LuresUser;

import java.util.Date;

public interface UserManager {

    String GENDER_MALE = "man";
    String GENDER_FEMALE = "woman";

//    void initializeUser(@NonNull String userId,
//                        @NonNull final String displayName,
//                        @Nullable CompleteCallback completeCallback,
//                        @Nullable FailureCallback failureCallback);

    //LuresUser getCurrentUser();

    void getUser(@NonNull String userId,
                 @NonNull SuccessCallback<LuresUser> callback,
                 @Nullable FailureCallback failureCallback);

    void updateLocation(@NonNull Location location,
                        @Nullable CompleteCallback completeCallback,
                        @Nullable FailureCallback failureCallback);

    void setSpotifyId(@NonNull String spotifyId,
                      @Nullable CompleteCallback completeCallback,
                      @Nullable FailureCallback failureCallback);

    void setUserImageUri(@NonNull String imageUri,
                         @Nullable CompleteCallback completeCallback,
                         @Nullable FailureCallback failureCallback);

    void setBirthdate(@NonNull Date date,
                      @Nullable CompleteCallback completeCallback,
                      @Nullable FailureCallback failureCallback);

    void setAboutMe(@NonNull String caption,
                    @Nullable CompleteCallback completeCallback,
                    @Nullable FailureCallback failureCallback);

    void setEmail(@NonNull String email,
                  @Nullable CompleteCallback completeCallback,
                  @Nullable FailureCallback failureCallback);

    void setGender(@NonNull String genderPreferences,
                   @Nullable CompleteCallback completeCallback,
                   @Nullable FailureCallback failureCallback);

    void createUser(@NonNull String userId,
                    @NonNull String displayName,
                    @Nullable SuccessCallback<LuresUser> successCallback,
                    @Nullable FailureCallback failureCallback);

}
