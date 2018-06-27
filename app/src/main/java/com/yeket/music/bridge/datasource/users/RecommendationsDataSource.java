package com.yeket.music.bridge.datasource.users;

import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;


/**
 * Created by santoni7 on 30.09.17.
 */

public interface RecommendationsDataSource {

    interface ApiClient {
        @GET("/recommendations/{id}")
        void listRecommendationIds(@Path("id") String userId, Callback<List<String>> callback);
    }

    void getAllRecommendations(String userId,
                               SuccessCallback<LuresUser> nextUserReceived,
                               FailureCallback failureCallback,
                               CompleteCallback completeCallback);
}
