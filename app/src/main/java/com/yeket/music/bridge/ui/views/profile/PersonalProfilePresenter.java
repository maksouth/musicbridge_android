package com.yeket.music.bridge.ui.views.profile;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yeket.music.bridge.datasource.music.FirebaseMusicDataSource;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.datasource.tokens.FirebaseTokensDataSource;
import com.yeket.music.bridge.datasource.tokens.TokensDataSource;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.infrastructure.utils.DateUtils;
import com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.services.AuthManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class PersonalProfilePresenter implements PersonalProfileContract.Presenter {

    private AuthManager authManager;
    private TokensDataSource tokensDataSource;
    private MusicDataSource musicDataSource;
    private List<String> favoriteGenres;

    private LuresUser currentUser;

    private PersonalProfileContract.View view;

    @Inject
    public PersonalProfilePresenter(PersonalProfileContract.View view,
                                    LuresUser currentUser,
                                    AuthManager authManager,
                                    TokensDataSource tokensDataSource,
                                    MusicDataSource musicDataSource) {
        this.view = view;
        this.currentUser = currentUser;
        this.authManager = authManager;
        this.tokensDataSource = tokensDataSource;
        this.musicDataSource = musicDataSource;
        favoriteGenres = new ArrayList<>();
    }

    public void start(){
        Date userBirthdate = new Date(currentUser.getBirthdate());
        String userAgeStr = String.valueOf( DateUtils.getDiffYears( userBirthdate, new Date() ) );

        view.setAge(userAgeStr);
        view.setName(currentUser.getDisplayName());
        view.setImage(currentUser.getImageUri());

        musicDataSource.getFavoriteGenres(currentUser.getId(), data -> {
            favoriteGenres = data;
            view.showGenres(data);
        }, errorResponse -> view.showError(errorResponse.message));
    }

    @Override
    public void editButtonClicked() {
        view.goToEditProfileScreen();
    }

    @Override
    public void logoutButtonClicked() {
        authManager.logout();
        tokensDataSource.removeFirebaseToken(currentUser.getId());
        view.goToLoginScreen();
    }

    @Override
    public void editGenresButtonClicked() {
        musicDataSource.getGenres(new SuccessCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        view.goEditGenresScreen(data, favoriteGenres);
                    }
                }.sendEmptyMessage(120);

            }
        }, errorResponse -> view.showError(errorResponse.message));
    }

    @Override
    public void editAppSettingsClicked() {
        view.goEditAppSettingsScreen();
    }
}
