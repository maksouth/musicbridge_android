package com.yeket.music.bridge.infrastructure.callbacks;

import com.yeket.music.bridge.models.error.ErrorResponse;

@FunctionalInterface
public interface FailureCallback {
    // TODO: 03.01.18 maybe add logging by default??
    void onFailure(ErrorResponse errorResponse);
}
