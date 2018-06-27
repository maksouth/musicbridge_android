package com.yeket.music.bridge.datasource.users;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.LuresUser;

import java.util.List;

public interface UsersDataSource {

    void getUser(@NonNull String id,
                 @NonNull SuccessCallback<LuresUser> callback,
                 @Nullable FailureCallback failureCallback);

    void createUser(@NonNull String id,
                    @NonNull String displayName,
                    @Nullable CompleteCallback callback,
                    @Nullable FailureCallback failureCallback);

    void getAllUsers(@NonNull SuccessCallback<List<LuresUser>> listener,
                     @Nullable FailureCallback failureCallback);

    <T> void changeUserProperty(@NonNull String userId,
                                @NonNull String property,
                                @NonNull T value,
                                @Nullable CompleteCallback callback,
                                @Nullable FailureCallback failureCallback);
}
