package com.yeket.music.bridge.services.player;

import android.media.MediaPlayer;
import android.util.Log;

import com.yeket.music.R;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.models.Track;

import java.io.IOException;

import javax.inject.Inject;

import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.media.AudioManager.STREAM_MUSIC;

public class Player implements MediaPlayer.OnPreparedListener,
                                MediaPlayer.OnCompletionListener{

    private static final String TAG = Player.class.getSimpleName();
    private static final String ARTIST_SEPARATOR = ", ";
    private static final String AND = " & ";
    private static final String EMPTY_STRING = "";

    private enum PlayerState{
        WAITING_TO_PLAY, PREPARED, PLAYING, PAUSED, STOPPED
    }

    private MediaPlayer mediaPlayer;
    private PlayerState playerState = PlayerState.STOPPED;

    private MusicDataSource musicDataSource;
    private SpotifyService spotifyService;

    private PlayerContract.View view;

    @Inject
    public Player(PlayerContract.View view,
                  MusicDataSource musicDataSource,
                  SpotifyService spotifyService){

        this.view = view;

        this.musicDataSource = musicDataSource;
        this.spotifyService = spotifyService;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(STREAM_MUSIC);

    }

    public void prepareNextUserTrack(String userId){
        mediaPlayer.stop();
        mediaPlayer.reset();
        playerState = PlayerState.STOPPED;
        view.changePlayerButton(false);
        view.setArtistName(EMPTY_STRING);
        view.setTrackName(EMPTY_STRING);
        prepareTrack(userId);
    }

    public void click(String userId){

        switch (playerState){
            case PLAYING:
                mediaPlayer.pause();
                playerState = PlayerState.PAUSED;
                view.changePlayerButton(false);
                break;

            case PREPARED:
            case PAUSED:
                mediaPlayer.start();
                playerState = PlayerState.PLAYING;
                view.changePlayerButton(true);
                break;

            case STOPPED:
                playerState = PlayerState.WAITING_TO_PLAY;
                prepareTrack(userId);
                break;

            case WAITING_TO_PLAY:
                view.showProgressBar(R.string.progress_preparing_track);

        }

    }

    public void stop(){

        mediaPlayer.stop();
        mediaPlayer.reset();
        playerState = PlayerState.STOPPED;
        view.changePlayerButton(false);

    }

    public void release(){
        mediaPlayer.release();
    }

    private void preparePlayer(String previewUrl){

        try {
            Log.d(TAG, "Start player preparing " + mediaPlayer + " " + previewUrl);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(previewUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            view.showError(R.string.error_player_not_working);
            Log.e(TAG, "Player preparation failed", e);
            // TODO: 26.08.17 handle
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Player prepared " + mp);

        switch (playerState) {

            case WAITING_TO_PLAY:
                view.dismissProgressDialog();
                view.changePlayerButton(true);
                mediaPlayer.start();
                playerState = PlayerState.PLAYING;
                break;

            default:
                playerState = PlayerState.PREPARED;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        view.changePlayerButton(false);
    }

    private void prepareTrack(String userId) {
        musicDataSource.getFavoriteTrack(userId, this::onGetTrackSuccess, LoggerFailureCallback.EMPTY);
    }

    private void onGetTrackSuccess(Track track){
        spotifyService.getTrack(track.getSpotifyId(), new Callback<kaaes.spotify.webapi.android.models.Track>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.Track track, Response response) {
                StringBuilder artists = new StringBuilder();

                artists.append(track.artists.get(0).name);

                for (int i = 1; i < track.artists.size() - 1; i++) {
                    artists.append(ARTIST_SEPARATOR).append(track.artists.get(i).name);
                }

                if (track.artists.size() > 1) {
                    artists.append(AND)
                            .append(track.artists.get(track.artists.size() - 1).name);
                }

                view.setArtistName(artists.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error while getting track: " + error.getMessage(), error.getCause());
                // TODO: 24.09.17 add crashlytics
            }
        });

        view.setTrackName(track.getName());
        preparePlayer(track.getPreviewUrl());
    }

}
