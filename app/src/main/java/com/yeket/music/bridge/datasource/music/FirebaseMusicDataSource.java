package com.yeket.music.bridge.datasource.music;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.models.Artist;
import com.yeket.music.bridge.models.Track;
import com.yeket.music.bridge.models.error.ErrorResponse;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.yeket.music.bridge.datasource.util.Schemes.Music;

public class FirebaseMusicDataSource implements MusicDataSource {
    
    private static final String TAG = FirebaseMusicDataSource.class.getSimpleName();

    private DatabaseReference mDatabaseTracks;
    private DatabaseReference mDatabaseArtists;
    private DatabaseReference mDatabaseGenres;
    private DatabaseReference mUserGenresNode;
    private DatabaseReference mAllGenresNode;

    @Inject
    public FirebaseMusicDataSource(){
        mDatabaseTracks = FirebaseDatabase.getInstance().getReference().child(Music.TRACKS_NODE_NAME);
        mDatabaseTracks.keepSynced(true);

        mDatabaseArtists = FirebaseDatabase.getInstance().getReference().child(Music.ARTISTS_NODE_NAME);
        mDatabaseArtists.keepSynced(true);

        mDatabaseGenres = FirebaseDatabase.getInstance().getReference().child(Music.GENRES_NODE_NAME);
        mDatabaseGenres.keepSynced(true);

        mAllGenresNode = mDatabaseGenres.child(Music.GENRES_ALL);
        mUserGenresNode = mDatabaseGenres.child(Music.GENRES_USERS);
    }

    @Override
    public void getTracks(@NotNull String userId,
                          @NonNull SuccessCallback<List<Track>> callback,
                          @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        if(userId ==  null) {
            failureCallback.onFailure(Errors.GET_TRACKS_USER_ID_NULL);
            return;
        }

        mDatabaseTracks.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    List<Track> trackList = new ArrayList<>();
                    Track temp;

                    Iterable<DataSnapshot> tracksSnapshot = dataSnapshot.getChildren();
                    for(DataSnapshot trackSnapshot : tracksSnapshot){
                        temp = getTrackFromSnapshot(trackSnapshot.getChildren().iterator().next());
                        trackList.add(temp);
                    }

                    if(trackList.size() == 0){
                        finalFailureCallback.onFailure(Errors.GET_TRACKS_NOT_EXIST);
                    }else{
                        callback.onSuccess(trackList);
                    }
                } else {
                    finalFailureCallback.onFailure(Errors.GET_TRACKS_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.GET_TRACKS_CANCELLED);
            }
        });
    }

    @Override
    public void addTrack(@NotNull String userId,
                         @Nullable Track track,
                         @Nullable CompleteCallback completeCallback,
                         @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);
        CompleteCallback finalCompleteCallback;

        if (userId ==  null) {
            finalFailureCallback.onFailure(Errors.WRITE_TRACK_USER_ID_NULL);
            return;
        } else if (track == null) {
            finalFailureCallback.onFailure(Errors.WRITE_TRACK_NULL_VALUE);
            return;
        }

        finalCompleteCallback = completeCallback == null
                ? ()->{}
                : completeCallback;

        mDatabaseTracks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabaseTracks.child(userId).removeValue();
                DatabaseReference userRef = mDatabaseTracks.child(userId);
                DatabaseReference trackRef;

                    trackRef = userRef.child(track.getSpotifyId());
                    trackRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {

                            mutableData.child(Music.TRACK_NAME).setValue(track.getName());
                            mutableData.child(Music.TRACK_DURATION_MS).setValue(track.getDurationMs());
                            mutableData.child(Music.TRACK_PREVIEW_URL).setValue(track.getPreviewUrl());
                            mutableData.child(Music.TRACK_IMAGE_URL).setValue(track.getImageUrl());

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            if(databaseError == null){
                                finalCompleteCallback.onComplete();
                            } else finalFailureCallback.onFailure(new ErrorResponse(Errors.WRITE_TRACK_FAIL, databaseError.getMessage()));
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.WRITE_TRACK_CANCELLED);
            }
        });

    }

    @Override
    public void getFavoriteTrack(@NotNull String userId,
                                 @NonNull SuccessCallback<Track> callback,
                                 @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        if (userId ==  null) {
            finalFailureCallback.onFailure(Errors.GET_TRACK_USER_ID_NULL);
            return;
        }

        Query query = mDatabaseTracks.child(userId).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Track track = getTrackFromSnapshot(dataSnapshot.getChildren().iterator().next());
                    Log.d(TAG, "Track: " + track.getPreviewUrl());
                    callback.onSuccess(track);
                } else {
                    finalFailureCallback.onFailure(Errors.GET_TRACK_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.GET_TRACK_CANCELLED);
            }
        });
    }

    @Override
    public void getArtists(@NotNull String userId,
                           int limit,
                           @NonNull SuccessCallback<List<Artist>> callback,
                           @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        if(userId ==  null) {
            finalFailureCallback.onFailure(Errors.GET_ARTISTS_USER_ID_NULL);
            return;
        }

        mDatabaseArtists.child(userId)
                        .limitToFirst(limit)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    List<Artist> artistList = new ArrayList<>();
                    Artist temp;

                    Iterable<DataSnapshot> artistSnapIterator = dataSnapshot.getChildren();
                    for(DataSnapshot artistSnapshot : artistSnapIterator){
                        temp = readArtistFromSnapshot(artistSnapshot);//artistSnapshot.getValue(Artist.class);
                        artistList.add(temp);
                    }

                    if(artistList.size() == 0){
                        finalFailureCallback.onFailure(Errors.GET_ARTISTS_NOT_EXIST);
                    }else{
                        callback.onSuccess(artistList);
                    }
                } else {
                    finalFailureCallback.onFailure(Errors.GET_ARTISTS_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.GET_ARTISTS_CANCELLED);
            }
        });
    }

    @Override
    public void addArtists(@NotNull String userId,
                           @NonNull List<Artist> artists,
                           @Nullable CompleteCallback callback,
                           @Nullable FailureCallback failureCallback) {

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);
        CompleteCallback completeCallback = CompleteCallback.notNull(callback);

        if(userId ==  null) {
            finalFailureCallback.onFailure(Errors.WRITE_ARTISTS_USER_ID_NULL);
            return;
        }

        mDatabaseArtists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(Artist artist : artists){
                    mDatabaseArtists.child(userId)
                        .child(artist.getSpotifyId())
                        .setValue(artist)
                        .addOnCompleteListener(task -> completeCallback.onComplete())
                        .addOnFailureListener(e -> finalFailureCallback.onFailure(
                                new ErrorResponse(Errors.WRITE_ARTISTS_FAIL, e.getMessage())));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.WRITE_ARTISTS_CANCELLED);
            }
        });
    }

    // TODO: 22.10.17 finish with artists!
    @Override
    public void getFavoriteArtists(@NotNull String userId,
                                   @NonNull SuccessCallback<List<Artist>> listener,
                                   @Nullable FailureCallback failureCallback) {
        if(userId ==  null) {
            failureCallback.onFailure(Errors.GET_ARTISTS_USER_ID_NULL);
            return;
        }
//        Query query = mDatabaseArtists.child(userId).equalTo(true).limitToFirst(5);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//
//                    for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
//
//                    }
//
//                    if (dataSnapshot != null) {
//                        Artist artist = dataSnapshot.getChildren().iterator().next().getValue(Artist.class);
//                        listener.onSuccess(artist);
//                    } else {
//                        listener.onFailure(ERROR_NO_VALUE_FOUND);
//                    }
//                }catch (Exception e){
//                    Log.e("FirebaseMusicDataSource", "getFavoriteArtists: " + e);
//                    listener.onFailure(ERROR_NO_VALUE_FOUND);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                listener.onFailure(ERROR_REQUEST_CANCELED);
//            }
//        });
    }

    public void setFavoriteGenres(@NotNull String userId, @NonNull List<String> favoriteGenres){
        if(userId ==  null) {
            return;
        }
        mUserGenresNode.child(userId).removeValue();

        DatabaseReference userGenresReference = mUserGenresNode.child(userId);
        for(String genre : favoriteGenres){
            userGenresReference.child(genre).setValue(true);
        }
    }

    public void getFavoriteGenres(@NonNull String userId,
                                  @NonNull SuccessCallback<List<String>> listener,
                                  @Nullable FailureCallback failureCallback){

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        if(userId ==  null) {
            finalFailureCallback.onFailure(Errors.GET_GENRES_USER_ID_NULL);
            return;
        }

        Query query = mUserGenresNode.child(userId);
        getGenresInternally(query, listener, finalFailureCallback);
    }

    @Override
    public void getGenres(int limit,
                          @NonNull SuccessCallback<List<String>> callback,
                          @Nullable FailureCallback failureCallback) {
        Query query = mAllGenresNode.limitToFirst(limit);
        getGenresInternally(query, callback, failureCallback);
    }

    @Override
    public void getGenres(@NonNull SuccessCallback<List<String>> callback,
                          @Nullable FailureCallback failureCallback){
        getGenresInternally(mAllGenresNode, callback, failureCallback);
    }

    private Artist readArtistFromSnapshot(final DataSnapshot snapshot){
        Artist artist = new Artist();

        String spotifyId = snapshot.getKey();

        Object imageUrlObject = snapshot.child(Music.ARTIST_IMAGE_URL).getValue();
        String imageUrl = imageUrlObject == null ? null : imageUrlObject.toString();

        Object nameObject = snapshot.child(Music.ARTIST_NAME).getValue();
        String name = nameObject == null ? null : nameObject.toString();

        artist.setName(name);
        artist.setImageUrl(imageUrl);
        artist.setSpotifyId(spotifyId);

        return artist;
    }

    private void getGenresInternally(Query query,
                                     @NonNull SuccessCallback<List<String>> callback,
                                     @Nullable FailureCallback failureCallback){

        FailureCallback finalFailureCallback = LoggerFailureCallback.notNull(failureCallback);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    List<String> genresList = new ArrayList<>();
                    String temp;

                    Iterable<DataSnapshot> genreSnapIterator = dataSnapshot.getChildren();
                    for(DataSnapshot genreSnapshot : genreSnapIterator){
                        temp = genreSnapshot.getKey();
                        genresList.add(temp);
                    }

                    if(genresList.size() == 0){
                        finalFailureCallback.onFailure(Errors.GET_GENRES_NOT_EXIST);
                    }else{
                        callback.onSuccess(genresList);
                    }
                } else {
                    finalFailureCallback.onFailure(Errors.GET_GENRES_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finalFailureCallback.onFailure(Errors.GET_GENRES_CANCELLED);
            }
        });
    }

    private Track getTrackFromSnapshot(DataSnapshot dataSnapshot){
        Track track = new Track();

        String spotifyId = dataSnapshot.getKey();
        String name = (String) dataSnapshot.child(Music.TRACK_NAME).getValue();
        Object durationValue = dataSnapshot.child(Music.TRACK_DURATION_MS).getValue();
        Long durationMs = durationValue == null ? Default.Music.TRACK_LENGTH_MS
                                                    : Long.valueOf(durationValue.toString());
        String imageUrl = (String) dataSnapshot.child(Music.TRACK_IMAGE_URL).getValue();
        String previewUrl = (String) dataSnapshot.child(Music.TRACK_PREVIEW_URL).getValue();

        track.setSpotifyId(spotifyId);
        track.setDurationMs(durationMs);
        track.setName(name);
        track.setImageUrl(imageUrl);
        track.setPreviewUrl(previewUrl);

        return track;
    }

}
