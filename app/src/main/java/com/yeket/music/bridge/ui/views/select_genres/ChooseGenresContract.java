package com.yeket.music.bridge.ui.views.select_genres;

import com.yeket.music.bridge.ui.views.BaseContract;

import java.util.List;

public interface ChooseGenresContract {

    interface View extends BaseContract.View{
        void goNextScreen();
    }

    interface Presenter{
        void saveClicked(List<String> selectedGenres);
    }

}
