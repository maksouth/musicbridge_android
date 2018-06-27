package com.yeket.music.bridge.ui.views.main;

import android.content.Intent;

import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.services.LocationService;
import com.yeket.music.bridge.services.UserManager;

import javax.inject.Inject;

import static com.yeket.music.bridge.services.notifications.constants.NotificationConstants.USER_1;
import static com.yeket.music.bridge.services.notifications.constants.NotificationConstants.USER_2;

public class MainPresenter implements MainContract.Presenter{

    public static final String TAG = MainPresenter.class.getSimpleName();

    private enum Fragments {
        HOME, CHATS, PROFILE
    }

    private Fragments currentFragment = Fragments.HOME;
    private MainContract.View view;
    private LocationService locationService;
    private UserManager userManager;
    private LuresUser user;

    @Inject
    public MainPresenter(MainContract.View view,
                         LuresUser user,
                         LocationService locationService,
                         UserManager userManager) {
        this.view = view;
        this.user = user;
        this.locationService = locationService;
        this.userManager = userManager;
    }

    @Override
    public void start() {

        locationService.getUserLocation(
                location -> userManager.updateLocation(location, null, LoggerFailureCallback.EMPTY),
                LoggerFailureCallback.EMPTY);

        view.goToHomeScreen();
    }

    @Override
    public void currentUserProfileButtonClicked() {
        if(!Fragments.PROFILE.equals(currentFragment)) {
            currentFragment = Fragments.PROFILE;
            view.goToCurrentUserProfile();
        }
    }

    @Override
    public void chatButtonClicked() {
        if(!Fragments.CHATS.equals(currentFragment)) {
            currentFragment = Fragments.CHATS;
            view.goToChatsScreen();
        }
    }

    @Override
    public void homeButtonClicked() {
        if(!Fragments.HOME.equals(currentFragment)) {
            currentFragment = Fragments.HOME;
            view.goToHomeScreen();
        }

    }

    @Override
    public void pause() {
        locationService.onPause();

    }

    @Override
    public void resume() {
        locationService.onResume();
    }


    @Override
    public void broadcastReceived(Intent intent) {
        String userId1 = intent.getStringExtra(USER_1);
        String userId2 = intent.getStringExtra(USER_2);

        String currentUserId;
        String likedUserId;

        if(user.getId().equals(userId1)){
            currentUserId = userId1;
            likedUserId = userId2;
        } else {
            currentUserId = userId2;
            likedUserId = userId1;
        }

        view.showMutualLikesDialog(currentUserId, likedUserId);
    }

    @Override
    public void fetchActivityResults(int requestCode, int resultCode, Intent data) {
        locationService.fetchActivityResults(requestCode, resultCode, data);
    }

    @Override
    public void fetchRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        locationService.fetchRequestPermissionResults(requestCode, permissions, grantResults);
    }

}
