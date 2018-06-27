package com.yeket.music.bridge.ui.views.main;

import android.content.Intent;

public class MainContract {

    public static final String CURRENT_USER_ID = "current_user_id";
    public static final String LIKED_USER_ID = "liked_user_id";

    public interface View{
        void goToCurrentUserProfile();
        void goToChatsScreen();
        void goToHomeScreen();
        void showMutualLikesDialog(String currentUserId, String likedUserId);

    }

    public interface Presenter{
        void start();
        void currentUserProfileButtonClicked();
        void chatButtonClicked();
        void homeButtonClicked();
        void pause();
        void resume();
        void fetchActivityResults(int requestCode, int resultCode, Intent data);
        void fetchRequestPermissionResult(int requestCode, String permissions[], int[] grantResults);
        void broadcastReceived(Intent intent);
    }
}
