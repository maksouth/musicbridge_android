package com.yeket.music.bridge.infrastructure.callbacks;

import android.support.annotation.Nullable;
import android.util.Log;

import com.yeket.music.bridge.models.error.ErrorResponse;

@FunctionalInterface
public interface LoggerFailureCallback extends FailureCallback{

    LoggerFailureCallback EMPTY = errorResponse -> {};

    String TAG = LoggerFailureCallback.class.getSimpleName();

    static FailureCallback notNull(@Nullable FailureCallback failureCallback){
        return failureCallback == null
                ? EMPTY
                : failureCallback;
    }

    default void onFailure(ErrorResponse errorResponse){
        Log.d(TAG, errorResponse.toString());
        failure(errorResponse);
    }

    void failure(ErrorResponse errorResponse);

}
