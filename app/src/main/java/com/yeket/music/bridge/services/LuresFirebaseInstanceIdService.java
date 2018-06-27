package com.yeket.music.bridge.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yeket.music.bridge.datasource.tokens.TokensDataSource;
import com.yeket.music.bridge.infrastructure.di.components.DaggerFirebaseInstanceIdServiceComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.ui.views.LuresApplication;

import javax.inject.Inject;

public class LuresFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = LuresFirebaseInstanceIdService.class.getSimpleName();

    @Inject
    TokensDataSource tokensDataSource;
    @Inject
    LuresUser user;

    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();

        DaggerFirebaseInstanceIdServiceComponent
                .builder()
                .singletonComponent(((LuresApplication)getApplication()).getSingletonComponent())
                .contextModule(new ContextModule(getApplicationContext()))
                .build()
                .inject(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        tokensDataSource.addFirebaseToken(user == null ? null : user.getId(), refreshedToken);
    }

}
