package com.yeket.music.bridge.datasource.likes;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.LuresUser;

import java.util.Date;

import javax.inject.Inject;

public class FirebaseLikesDataSource implements LikesDataSource {

    private DatabaseReference mDatabaseLikes;
    private DatabaseReference mDatabaseDislikes;

    @Inject
    public FirebaseLikesDataSource(){

        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child(Schemes.Likes.LIKES_NODE_NAME);
        mDatabaseLikes.keepSynced(true);

        mDatabaseDislikes = FirebaseDatabase.getInstance().getReference().child(Schemes.Dislikes.DISLIKES_NODE_NAME);
        mDatabaseDislikes.keepSynced(true);

    }

    @Override
    public void like(@NonNull String whoId, @NonNull String byWhomId) {
        mDatabaseLikes.child(whoId).child(byWhomId).setValue(new Date().getTime());
    }

    @Override
    public void dislike(@NonNull String whoId, @NonNull String byWhomId) {
        mDatabaseDislikes.child(byWhomId).child(whoId).setValue(new Date().getTime());
    }

    @Override
    public void isUserLiked(@NonNull String whoId, @NonNull String byWhomId,
                            @NonNull SuccessCallback<Boolean> callback) {
        mDatabaseLikes.child(whoId).child(byWhomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onSuccess(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onSuccess(false);
            }
        });
    }

    @Override
    public void isUserDisliked(@NonNull String whoId, @NonNull String byWhomId, @NonNull SuccessCallback<Boolean> listener) {
        mDatabaseDislikes.child(byWhomId).child(whoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onSuccess(false);
            }
        });
    }

}
