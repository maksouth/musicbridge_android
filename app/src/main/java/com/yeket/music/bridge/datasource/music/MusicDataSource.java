package com.yeket.music.bridge.datasource.music;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.models.Artist;
import com.yeket.music.bridge.models.Track;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import java.util.List;

public interface MusicDataSource {

    void getTracks(@NonNull String userId,
                   @NonNull SuccessCallback<List<Track>> callback,
                   @Nullable FailureCallback failureCallback);

    void addTrack(@NonNull String userId,
                  @Nullable Track track,
                  @Nullable CompleteCallback completeCallback,
                  @Nullable FailureCallback failureCallback);

    void getFavoriteTrack(@NonNull String userId,
                          @NonNull SuccessCallback<Track> successCallback,
                          @Nullable FailureCallback failureCallback);

    void getArtists(@NonNull String userId,
                    int limit,
                    @NonNull SuccessCallback<List<Artist>> callback,
                    @Nullable FailureCallback failureCallback);

    void addArtists(@NonNull String userId,
                    @NonNull List<Artist> artists,
                    @Nullable CompleteCallback completeCallback,
                    @Nullable FailureCallback failureCallback);

    void getFavoriteArtists(@NonNull String userId,
                            @NonNull SuccessCallback<List<Artist>> callback,
                            FailureCallback failureCallback);

    void setFavoriteGenres(@NonNull String userId, @NonNull List<String> genres);

    void getGenres(int limit,
                   @NonNull SuccessCallback<List<String>> listener,
                   @Nullable FailureCallback failureCallback);

    void getGenres(@NonNull SuccessCallback<List<String>> callback,
                   @Nullable FailureCallback failureCallback);

    void getFavoriteGenres(@NonNull String userId,
                           @NonNull SuccessCallback<List<String>> listener,
                           @Nullable FailureCallback failureCallback);
}
