package com.yeket.music.bridge.ui.views.home;

import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.ui.views.BaseContract;

/**
 * Created by maksouth on 04.09.17.
 */

public interface HomeContract {

    public interface View extends BaseContract.View{
        void addData(LuresUser dataSet);
        void swipeLeft();
        void swipeRight();
        void showOverlay(boolean showMessage);
        void hideOverlay();
    }

    public interface Presenter{
        void start();
        void pause();
        void cardSwipeLike();
        void cardClick(int position);
        void cardSwipeDislike();
        void likeButtonClick();
        void dislikeButtonClick();
        void playerClicked();
        void destroy();
    }

}
