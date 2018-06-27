package com.yeket.music.bridge.datasource.users;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.models.AgeRange;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

public interface UserPreferencesSource {

    void setMinAgeRange(@NonNull String userId, int value,
                        @Nullable CompleteCallback callback,
                        @Nullable FailureCallback failureCallback);

    void setMaxAgeRange(@NonNull String userId,
                        int value,
                        @Nullable CompleteCallback callback,
                        @Nullable FailureCallback failureCallback);

    void getAgeRange(@NonNull String userId,
                     @NonNull SuccessCallback<AgeRange> callback,
                     @Nullable FailureCallback failureCallback);

}
