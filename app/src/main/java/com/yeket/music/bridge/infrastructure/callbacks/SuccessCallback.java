package com.yeket.music.bridge.infrastructure.callbacks;

@FunctionalInterface
public interface SuccessCallback<T> {
    void onSuccess(T data);
}
