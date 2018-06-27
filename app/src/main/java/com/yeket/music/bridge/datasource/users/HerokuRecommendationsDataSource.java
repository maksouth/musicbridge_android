package com.yeket.music.bridge.datasource.users;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by santoni7 on 30.09.17.
 */

public class HerokuRecommendationsDataSource implements RecommendationsDataSource {

    private static final String TAG = HerokuRecommendationsDataSource.class.getSimpleName();

    private UsersDataSource usersDataSource;
    private ApiClient apiClient;

    private HerokuRecommendationsDataSource(UsersDataSource usersDataSource) {
        this.usersDataSource = usersDataSource;
        String API_BASE_URL = "http://santo-chatty.herokuapp.com/";

        RestAdapter.Builder builder =
                new RestAdapter.Builder()
                        .setEndpoint(API_BASE_URL)
                        .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        apiClient = adapter.create(ApiClient.class);
    }

    private int usersReceived = 0;
    @Override
    public void getAllRecommendations(@NotNull String userId,
                                      @NonNull SuccessCallback<LuresUser> nextUserReceivedListener,
                                      @NonNull FailureCallback failureCallback,
                                      @NonNull CompleteCallback completeCallback) {
        usersReceived = 0;

        Log.d(TAG, "getAllRecommendations");

        apiClient.listRecommendationIds(userId, new Callback<List<String>>() {

            @Override
            public void success(List<String> strings, Response response) {
                Log.d(TAG, "success");
                for(String s : strings){

                    @SuppressWarnings("StringEquality") //Compare by reference. TODO check if it works
                    final boolean isLast = s == strings.get(strings.size()-1);

                    usersDataSource.getUser(s, data -> {
                            usersReceived++;
                            nextUserReceivedListener.onSuccess(data);
                        },

                        errorResponse -> {
                            failureCallback.onFailure(errorResponse);
                            if(isLast && usersReceived == 0){
                                failureCallback.onFailure(Errors.GET_RECOMMENDATIONS_NOT_FOUND);
                            }
                        });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "getAllRecommendations: failure\nReason: " + error.getMessage());
                failureCallback.onFailure(Errors.GET_RECOMMENDATIONS_NOT_FOUND);
            }

        });
    }
}
