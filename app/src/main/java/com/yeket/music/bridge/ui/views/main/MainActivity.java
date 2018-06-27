package com.yeket.music.bridge.ui.views.main;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.services.LuresFirebaseMessagingService;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.dialogs.DialogListFragment;
import com.yeket.music.bridge.ui.views.home.HomeFragment;
import com.yeket.music.bridge.ui.views.match_dialog.MatchFullScreenDialog;
import com.yeket.music.bridge.ui.views.profile.PersonalProfileFragment;

import static com.yeket.music.bridge.ui.views.main.MainContract.CURRENT_USER_ID;
import static com.yeket.music.bridge.ui.views.main.MainContract.LIKED_USER_ID;

public class MainActivity extends AppCompatActivity
                            implements BottomNavigationView.OnNavigationItemSelectedListener,
                            MainContract.View{

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainContract.Presenter presenter;

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        initialize();
    }

    private void initialize(){

        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(this))
                .build()
                .mainPresenter();

        presenter.start();
    }

    private void initializeUI(){

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void goToCurrentUserProfile() {
        PersonalProfileFragment fragment = new PersonalProfileFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void goToChatsScreen() {
        DialogListFragment fragment = new DialogListFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void goToHomeScreen() {
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    @Override
    public void showMutualLikesDialog(String currentUserId, String likedUserId) {
        DialogFragment fragment = new MatchFullScreenDialog();

        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_USER_ID, currentUserId);
        bundle.putString(LIKED_USER_ID, likedUserId);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), fragment.toString());

        // TODO: 10.09.17 sooo hack
        View view = bottomNavigationView.findViewById(R.id.action_chats);
        view.performClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        presenter.fetchRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(LuresFirebaseMessagingService.ON_MESSAGE_RECEIVED_EVENT));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.fetchActivityResults(requestCode, resultCode, data);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.broadcastReceived(intent);
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_chats:
                presenter.chatButtonClicked();
                break;

            case R.id.action_likes:
                presenter.homeButtonClicked();
                break;

            case R.id.action_profile:
                presenter.currentUserProfileButtonClicked();
                break;


        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Fragments in back stack " + fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
