package com.yeket.music.bridge.infrastructure.callbacks;

import android.support.annotation.Nullable;

@FunctionalInterface
public interface CompleteCallback {

    CompleteCallback EMPTY = () -> {};

    static CompleteCallback notNull(@Nullable CompleteCallback completeCallback){
        return completeCallback == null
                ? EMPTY
                : completeCallback;

    }

    void onComplete();
}
