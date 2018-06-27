package com.yeket.music.bridge.ui.views.details;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yeket.music.R;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.users.UserPreferencesSource;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.models.error.ErrorResponse;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.services.UserManager;

import javax.inject.Inject;

import static com.yeket.music.bridge.services.UserManager.GENDER_FEMALE;
import static com.yeket.music.bridge.services.UserManager.GENDER_MALE;

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;

    private UserManager userManager;
    private UserPreferencesSource userPreferencesSource;
    private LuresUser currentUser;
    private StorageReference imageStorage;

    private int minAge, maxAge;
    private boolean isBoy = true;

    @Inject
    public DetailsPresenter(DetailsContract.View view,
                            LuresUser user,
                            UserManager userManager,
                            UserPreferencesSource userPreferencesSource){
        this.view = view;
        this.imageStorage = FirebaseStorage.getInstance().getReference();
        this.userManager = userManager;
        this.userPreferencesSource = userPreferencesSource;
        this.currentUser = user;
    }

    public void start(){

        isBoy = currentUser.getGender() == null || currentUser.getGender().equals(GENDER_MALE);

        view.changeGenderLabel(isBoy);
        view.enableBoyIcon(isBoy);
        view.enableGirlIcon(!isBoy);

        userPreferencesSource.getAgeRange(currentUser.getId(), data -> {
            view.setAgeRangeBarBorders(data.getMinAge(), data.getMaxAge());
            view.setMinAgeRangeLabel(data.getMinAge());
            view.setMaxAgeRangeLabel(data.getMaxAge());
        }, (LoggerFailureCallback)errorResponse -> {
            view.setAgeRangeBarBorders(Default.Preferences.MIN_AGE, Default.Preferences.MAX_AGE);
            view.setMinAgeRangeLabel(Default.Preferences.MIN_AGE);
            view.setMaxAgeRangeLabel(Default.Preferences.MAX_AGE);
        });

        if( currentUser.getImageUri() != null ) {
            view.setProfileImage(currentUser.getImageUri());
        }

        if( currentUser.getAboutYou() != null ){
            view.setAboutYou(currentUser.getAboutYou());
        }
    }

    @Override
    public void nextButtonClicked() {

        view.showProgressBar(R.string.progress_saving_data);

        String genderPreferences = isBoy ? GENDER_MALE : GENDER_FEMALE;
        userManager.setGender(genderPreferences, null, LoggerFailureCallback.EMPTY);

        String userCaption = view.getAboutYouText();

        if(userCaption != null){
            userManager.setAboutMe(userCaption, null, LoggerFailureCallback.EMPTY);
        }


        userPreferencesSource.setMinAgeRange(currentUser.getId(), minAge, null, LoggerFailureCallback.EMPTY);
        userPreferencesSource.setMaxAgeRange(currentUser.getId(), maxAge, null, LoggerFailureCallback.EMPTY);

        view.goToNextScreen();
        view.dismissProgressDialog();

    }

    @Override
    public void backButtonClicked() {
        view.goToPreviousScreen();
    }

    @Override
    public void boyIconClicked() {
        if(!isBoy){
            isBoy = true;
            view.enableBoyIcon(true);
            view.enableGirlIcon(false);
            view.changeGenderLabel(isBoy);
        }
    }

    @Override
    public void girlIconClicked() {
        if(isBoy){
            isBoy = false;
            view.enableBoyIcon(false);
            view.enableGirlIcon(true);
            view.changeGenderLabel(isBoy);
        }
    }

    @Override
    public void profileImageClicked() {
        view.openImageChooser();
    }

    @Override
    public void fetchImageChooserResults(Uri imageUri) {

        view.showProgressBar(R.string.progress_updating_image);

        StorageReference profileImageReference = imageStorage.child("profile_images/" + currentUser.getId());
        profileImageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

            if(taskSnapshot.getDownloadUrl() == null){
                showError(Errors.WRITE_IMAGE_FAIL);
                return;
            }

            final String imageUrl = taskSnapshot.getDownloadUrl().toString();
            userManager.setUserImageUri(imageUrl, () ->{
                    view.setProfileImage(imageUrl);
                    view.dismissProgressDialog();
                }, (LoggerFailureCallback) this::showError);
        }).addOnFailureListener(e -> showError(Errors.WRITE_IMAGE_FAIL));
        // TODO: 03.01.18 log exception
    }

    @Override
    public void ageRangeChanged(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        view.setMinAgeRangeLabel(minAge);
        view.setMaxAgeRangeLabel(maxAge);
    }

    private void showError(ErrorResponse errorResponse){
        view.dismissProgressDialog();
        view.showError(errorResponse.message);
    }

}
