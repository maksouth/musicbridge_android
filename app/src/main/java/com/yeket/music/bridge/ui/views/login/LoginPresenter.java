package com.yeket.music.bridge.ui.views.login;

import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.yeket.music.R;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.datasource.settings.SettingsDataSource;
import com.yeket.music.bridge.datasource.tokens.TokensDataSource;
import com.yeket.music.bridge.datasource.users.FirebaseUserPreferencesSource;
import com.yeket.music.bridge.datasource.users.UserPreferencesSource;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.utils.DateUtils;
import com.yeket.music.bridge.infrastructure.utils.TypeConverter;
import com.yeket.music.bridge.services.AuthManager;
import com.yeket.music.bridge.services.SpotifyServiceHolder;
import com.yeket.music.bridge.services.UserManager;

import javax.inject.Inject;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.yeket.music.bridge.ui.views.login.LoginContract.REQUEST_CODE;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private SpotifyService spotifyService;
    private MusicDataSource musicDataSource;
    private UserManager userManager;
    private AuthManager authManager;
    private TokensDataSource tokensDataSource;
    private UserHolder userHolder;
    private SpotifyServiceHolder spotifyServiceHolder;
    private SettingsDataSource settingsDataSource;

    @Inject
    public LoginPresenter(LoginContract.View view,
                          MusicDataSource musicDataSource,
                          UserManager userManager,
                          SettingsDataSource settingsDataSource,
                          UserHolder userHolder,
                          AuthManager authManager,
                          TokensDataSource tokensDataSource,
                          SpotifyServiceHolder spotifyServiceHolder){
        this.view = view;
        this.userHolder = userHolder;
        this.musicDataSource = musicDataSource;
        this.userManager = userManager;
        this.authManager = authManager;
        this.tokensDataSource = tokensDataSource;
        this.spotifyServiceHolder = spotifyServiceHolder;
        this.settingsDataSource = settingsDataSource;
    }

    @Override
    public void start() {
        if(authManager.isSignedIn()) {
            loginSpotify();
        } else {
            view.setExplicitLoginView();
        }
    }

    @Override
    public void loginSpotify() {
        boolean showDialog = !authManager.isSignedIn();
        view.showSpotifyLogin(showDialog);
    }

    @Override
    public void fetchSpotifyLoginResults(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_CODE) {
            view.showProgressBar(R.string.progress_login);

            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                String accessToken = response.getAccessToken();
                Log.d("LoginPresenter", "Access token \'" + accessToken + "\'");
                spotifyServiceHolder.initialize(accessToken);
            }
        }

        spotifyService = spotifyServiceHolder.getService();
        if(spotifyService!=null) {
            spotifyService.getMe(new Callback<UserPrivate>() {
                @Override
                public void success(UserPrivate userPrivate, Response response) {
                    initializeUser(userPrivate);
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO: 26.07.17 error reporting and handling
                    Log.d("LoginPresenter", error.getMessage(), error.getCause());
                    view.dismissProgressDialog();
                    view.showError(R.string.error_cannot_load_spotify_profile);
                }
            });
        } else {
            view.dismissProgressDialog();
        }

    }

    private void initializeUser(final UserPrivate userPrivate){

        logUser(userPrivate);

        userManager.getUser(userPrivate.id,
                user -> onInitializeUserComplete(userPrivate.id),
                (LoggerFailureCallback)errorResponse -> {

                    if(errorResponse.equals(Errors.LOAD_USER_NOT_EXISTS)){

                        userManager.createUser(userPrivate.id, userPrivate.display_name, user -> {

                            userHolder.store(user);

                            addUserInfo(userPrivate);
                            saveTracks(userPrivate.id);
                            saveArtists(userPrivate.id);
                            setAgeRange(user.getId());
                            subscribeToNotifications(user.getId());

                            tokensDataSource.addFirebaseToken(user.getId());

                            finishLogin(user.getId());

                        }, err ->{
                            view.dismissProgressDialog();
                            view.showError(err.message);
                        });

                    } else {
                        view.dismissProgressDialog();
                        view.showError(R.string.error_failed_to_load_profile);
                    }

            });
    }

    private void subscribeToNotifications(String userId){
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.NEW_MESSAGE, true);
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.NEW_LIKE, true);
        settingsDataSource.updateSubscription(userId, SettingsDataSource.NotificationType.MATCH, true);
    }

    private void setAgeRange(String userId){
        UserPreferencesSource userPreferencesSource = new FirebaseUserPreferencesSource();
        userPreferencesSource.setMinAgeRange(userId,
                Default.Preferences.MIN_AGE,
                null, LoggerFailureCallback.EMPTY);
        userPreferencesSource.setMaxAgeRange(userId,
                Default.Preferences.MAX_AGE,
                null, LoggerFailureCallback.EMPTY);
    }

    private void addUserInfo(UserPrivate userPrivate){
        userManager.setSpotifyId(userPrivate.id, null, LoggerFailureCallback.EMPTY);
        userManager.setEmail(userPrivate.email, null, LoggerFailureCallback.EMPTY);
        userManager.setUserImageUri(userPrivate.images.get(0).url, null, LoggerFailureCallback.EMPTY);
        userManager.setBirthdate(DateUtils.getDate(userPrivate.birthdate), null, LoggerFailureCallback.EMPTY);
    }

    private void onInitializeUserComplete(String userId){

        saveTracks(userHolder.getUser().getId());
        saveArtists(userHolder.getUser().getId());

        tokensDataSource.addFirebaseToken(userId);

        finishLogin(userId);

    }

    /**
     * initialize user data in Crashlytics
     * @param userPrivate
     */
    private void logUser(UserPrivate userPrivate) {
        Crashlytics.setUserIdentifier(userPrivate.id);
        Crashlytics.setUserEmail(userPrivate.email);
        Crashlytics.setUserName(userPrivate.display_name);
    }


    private void finishLogin(String userId){
        authManager.loginWithSpotify(userId);

        // TODO: 10.09.17 update condition if needed
        if(userHolder.getUser().getGender() == null ||
                userHolder.getUser().getAboutYou() == null){
            view.goToDetailsActivity();
        } else {
            view.goToMainActivity();
        }

        view.dismissProgressDialog();
    }

    private void saveTracks(final String userId){
        if(spotifyService==null){
            return;
        }

        spotifyService.getMySavedTracks(new Callback<Pager<SavedTrack>>() {

            @Override
            public void success(Pager<SavedTrack> savedTrackPager, Response response) {
                musicDataSource.addTrack(userId,
                        TypeConverter.getLastValidTrack(savedTrackPager.items,
                                savedTrack -> savedTrack.track.preview_url != null
                                        && !savedTrack.track.preview_url.isEmpty()),
                        null,
                        LoggerFailureCallback.EMPTY);
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: 03.01.18 log all spotify errors
            }
        });
    }

    private void saveArtists(final String userId){
        if(spotifyService==null){
            return;
        }

        spotifyService.getFollowedArtists(new Callback<ArtistsCursorPager>() {
            @Override
            public void success(ArtistsCursorPager artistPager, Response response) {
                Log.d("LoginPresenter", "On get followed artists: " + artistPager.artists.items.size());
                musicDataSource.addArtists(userId,
                        TypeConverter.getArtists(artistPager.artists.items),
                        null,
                        LoggerFailureCallback.EMPTY);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("LoginPresenter", "On get followed artists failure: " + error.getLocalizedMessage(), error.getCause());
            }
        });

    }

}
