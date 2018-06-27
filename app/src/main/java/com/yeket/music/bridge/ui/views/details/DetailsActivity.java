package com.yeket.music.bridge.ui.views.details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.infrastructure.utils.SharedPreferencesWrapper;
import com.yeket.music.bridge.services.AuthManager;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.main.MainActivity;

public class DetailsActivity extends AppCompatActivity implements DetailsContract.View{

    private static final int SELECT_PICTURE_INTENT = 1337;

    public static final String PRESENTATION_MODE_KEY = "presentation_mode";
    public static final String EDIT_PROFILE_MODE = "edit_profile_mode";
    public static final String ONBOARDING_MODE = "onboarding_mode";

    private ProgressDialog progressDialog;
    private ConstraintLayout rootLayout;
    private ImageView boyIcon;
    private ImageView girlIcon;
    private ImageView profileImage;
    private TextView genderLabel;
    private EditText aboutYouText;
    private Button nextButton;
    private Button backButton;
    private RangeBar ageRangeBar;
    private TextView minAgeLabel;
    private TextView maxAgeLabel;

    private DetailsContract.Presenter presenter;

    private String presentationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        rootLayout = (ConstraintLayout) findViewById(R.id.root_layout);
        boyIcon = (ImageView) findViewById(R.id.boy_icon);
        girlIcon = (ImageView) findViewById(R.id.girl_icon);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        genderLabel = (TextView) findViewById(R.id.gender_label);
        aboutYouText = (EditText) findViewById(R.id.about_you_text);
        nextButton = (Button) findViewById(R.id.next_button);
        backButton = (Button) findViewById(R.id.back_button);
        ageRangeBar = (RangeBar) findViewById(R.id.age_rangebar);
        minAgeLabel = (TextView) findViewById(R.id.min_age_label);
        maxAgeLabel = (TextView) findViewById(R.id.max_age_label);

        presentationMode = getIntent().getStringExtra(PRESENTATION_MODE_KEY);

        if(ONBOARDING_MODE.equals(presentationMode)){
            nextButton.setText(R.string.next_step);
            backButton.setText(R.string.back);
        } else {
            nextButton.setText(R.string.save);
            backButton.setText(R.string.cancel);
        }

        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getApplication()).getSingletonComponent())
                .contextModule(new ContextModule(this))
                .viewModule(new ViewModule(this))
                .build().detailsPresenter();

        nextButton.setOnClickListener(v -> presenter.nextButtonClicked());

        backButton.setOnClickListener(v -> presenter.backButtonClicked());

        girlIcon.setOnClickListener(v -> presenter.girlIconClicked());

        boyIcon.setOnClickListener(v -> presenter.boyIconClicked());

        profileImage.setOnClickListener(v -> presenter.profileImageClicked());

        ageRangeBar.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            Log.d("DetailsActivity", "Range bar: " + leftPinIndex + " " + rightPinIndex + " " + leftPinValue + " " + rightPinValue);
            int min = Integer.valueOf(leftPinValue);
            int max = Integer.valueOf(rightPinValue);
            presenter.ageRangeChanged(min, max);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void enableBoyIcon(boolean enabled) {

        if(enabled){
            boyIcon.setBackground(getResources().getDrawable(R.drawable.boy_icon_pressed));
        } else {
            boyIcon.setBackground(getResources().getDrawable(R.drawable.boy_icon_normal));
        }

    }

    @Override
    public void enableGirlIcon(boolean enabled) {

        if(enabled){
            girlIcon.setBackground(getResources().getDrawable(R.drawable.girl_icon_pressed));
        } else {
            girlIcon.setBackground(getResources().getDrawable(R.drawable.girl_icon_normal));
        }

    }

    @Override
    public void setProfileImage(final String uri) {
        Picasso.with(DetailsActivity.this).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError() {
                Picasso.with(DetailsActivity.this).load(uri).into(profileImage);
            }
        });
    }

    @Override
    public void setAboutYou(String userCaption) {
        aboutYouText.setText(userCaption);
    }

    @Override
    public void changeGenderLabel(boolean isBoy) {

        if(isBoy){
            genderLabel.setText(R.string.boy);
        } else {
            genderLabel.setText(R.string.girl);
        }

    }

    @Override
    public String getAboutYouText() {
        return aboutYouText.getText().toString();
    }

    @Override
    public void goToPreviousScreen() {
        if (ONBOARDING_MODE.equals(presentationMode)) {

            AuthManager authManager = new AuthManager(new SharedPreferencesWrapper(getApplicationContext()));
            authManager.logout();

        }

        onBackPressed();
    }

    @Override
    public void goToNextScreen() {
        if(ONBOARDING_MODE.equals(presentationMode)) {
            Intent mainActivityIntent = new Intent(DetailsActivity.this, MainActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivityIntent);
        }

        finish();
    }

    @Override
    public void openImageChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE_INTENT);

    }

    @Override
    public void setAgeRangeBarBorders(final int leftValue, final int rightValue) {
        ageRangeBar.post(() -> ageRangeBar.setRangePinsByValue(leftValue, rightValue));
    }

    @Override
    public void setMinAgeRangeLabel(final int value) {
        minAgeLabel.post(() -> minAgeLabel.setText(String.valueOf(value)));
    }

    @Override
    public void setMaxAgeRangeLabel(final int value) {
        maxAgeLabel.post(() -> maxAgeLabel.setText(String.valueOf(value)));
    }

    @Override
    public void showError(int messageResourceId) {
        Snackbar.make(rootLayout, getString(messageResourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_INTENT) {
                Uri selectedImageUri = data.getData();
                presenter.fetchImageChooserResults(selectedImageUri);
            }
        }
    }

}
