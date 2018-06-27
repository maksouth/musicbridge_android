package com.yeket.music.bridge.datasource.likes;

import android.support.annotation.NonNull;

import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

public interface LikesDataSource {

    void like(@NonNull String whoId,
              @NonNull String byWhomId);

    void dislike(@NonNull String whoId,
                 @NonNull String byWhomId);

    void isUserLiked(@NonNull String whoId, @NonNull String byWhomId, @NonNull SuccessCallback<Boolean> listener);

    void isUserDisliked(@NonNull String whoId, @NonNull String byWhomId, @NonNull SuccessCallback<Boolean> listener);
}
