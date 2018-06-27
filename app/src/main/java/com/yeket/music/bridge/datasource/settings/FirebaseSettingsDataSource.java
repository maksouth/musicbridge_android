package com.yeket.music.bridge.datasource.settings;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.yeket.music.bridge.datasource.util.Checker;
import com.yeket.music.bridge.datasource.util.Schemes.Settings;

import javax.inject.Inject;

import io.reactivex.Single;

public class FirebaseSettingsDataSource implements SettingsDataSource{

    private DatabaseReference mUserSettingsNode;

    @Inject
    public FirebaseSettingsDataSource(){
        mUserSettingsNode = FirebaseDatabase.getInstance().getReference().child(Settings.SETTINGS_NODE_NAME);
        mUserSettingsNode.keepSynced(true);
    }

    @Override
    public Single<Boolean> isSubscribed(String userId, NotificationType type) {
        return Single.create(emmiter ->
            mUserSettingsNode.child(userId)
                .child(Settings.NOTIFICATIONS_NODE_NAME)
                .child(type.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean value = Checker.extract(dataSnapshot.getValue(), true);
                        emmiter.onSuccess(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emmiter.onError(databaseError.toException());
                    }
                })
        );
    }

    @Override
    public void updateSubscription(String userId, NotificationType type, boolean isSubscribed) {
//        return Completable.create(emitter->
            mUserSettingsNode.child(userId)
                .child(Settings.NOTIFICATIONS_NODE_NAME)
                .child(type.toString())
                .setValue(isSubscribed)
                .addOnSuccessListener(aVoid -> Log.d("SettingsDataSource", "succeed"))
                .addOnFailureListener(e -> Log.d("SettingsDataSource", "failed. reason " + e.getMessage()));
//        );
    }

}
