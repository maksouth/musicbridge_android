package com.yeket.music.bridge.ui.views.details;

import android.net.Uri;

import com.yeket.music.bridge.ui.views.BaseContract;

public interface DetailsContract {

    interface View extends BaseContract.View{

        void enableBoyIcon(boolean enabled);
        void enableGirlIcon(boolean enabled);
        void setProfileImage(String uri);
        void setAboutYou(String userCaption);
        void changeGenderLabel(boolean isBoy);
        String getAboutYouText();
        void goToPreviousScreen();
        void goToNextScreen();
        void openImageChooser();
        void setAgeRangeBarBorders(int leftValue, int rightValue);
        void setMinAgeRangeLabel(int value);
        void setMaxAgeRangeLabel(int value);
    }

    interface Presenter{
        void start();
        void nextButtonClicked();
        void backButtonClicked();
        void boyIconClicked();
        void girlIconClicked();
        void profileImageClicked();
        void fetchImageChooserResults(Uri ImageUri);
        void ageRangeChanged(int minAge, int maxAge);
    }

}
