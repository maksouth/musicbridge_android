package com.yeket.music.bridge.ui.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.details.DetailsActivity;
import com.yeket.music.bridge.ui.views.main.MainActivity;
import com.yeket.music.bridge.ui.views.privacy_policy.PrivacyPolicyActivity;

import static com.yeket.music.bridge.ui.views.details.DetailsActivity.ONBOARDING_MODE;
import static com.yeket.music.bridge.ui.views.details.DetailsActivity.PRESENTATION_MODE_KEY;
import static com.yeket.music.bridge.ui.views.login.LoginContract.REQUEST_CODE;

public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    LoginContract.Presenter presenter;
    private ProgressDialog progressDialog;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rootLayout = (ConstraintLayout) findViewById(R.id.root_layout);
        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(this))
                .build()
                .loginPresenter();

        presenter.start();

        if(findViewById(R.id.button)!=null)
            findViewById(R.id.button).setOnClickListener((v)->presenter.loginSpotify());

        if (findViewById(R.id.privacy_policy_label) != null) {
            findViewById(R.id.privacy_policy_label).setOnClickListener(
                    v -> startActivity(new Intent(this, PrivacyPolicyActivity.class))
            );
        }

    }

    @Override
    public void setExplicitLoginView() {
        setContentView(R.layout.activity_login_explicit_login);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.d("LoginActivity", message);
    }

    @Override
    public void goToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void goToDetailsActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, DetailsActivity.class);
        mainActivityIntent.putExtra(PRESENTATION_MODE_KEY, ONBOARDING_MODE);
        startActivity(mainActivityIntent);
    }

    @Override
    public void showSpotifyLogin(boolean showDialog) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(getString(R.string.spotify_client_id),
                AuthenticationResponse.Type.TOKEN, getString(R.string.spotify_redirect_uri));
        builder.setScopes(new String[]{ "user-read-private",
                "playlist-read-collaborative",
                "user-follow-read",
                "user-library-read",
                "user-read-birthdate",
                "user-read-email",
                "user-top-read" });
        if(showDialog){
            builder.setShowDialog(true);
        }

        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    public void showError(@StringRes int messageResourceId) {
        Toast.makeText(this, messageResourceId, Toast.LENGTH_SHORT).show();
        //runOnUiThread(() -> Snackbar.make(rootLayout, getString(messageResourceId), Snackbar.LENGTH_LONG).show());
    }

    @Override
    public void showProgressBar(int messageResourceId){
        if(progressDialog != null){
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(messageResourceId));
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        presenter.fetchSpotifyLoginResults(requestCode, resultCode, intent);
    }
}
