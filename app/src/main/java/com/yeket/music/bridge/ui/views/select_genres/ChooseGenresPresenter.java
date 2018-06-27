package com.yeket.music.bridge.ui.views.select_genres;

import com.yeket.music.R;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.models.LuresUser;

import java.util.List;

import javax.inject.Inject;

public class ChooseGenresPresenter implements ChooseGenresContract.Presenter {

    private ChooseGenresContract.View view;
    private String userId;
    private MusicDataSource musicDataSource;

    @Inject
    public ChooseGenresPresenter(ChooseGenresContract.View view,
                                 LuresUser user,
                                 MusicDataSource musicDataSource){
        this.view = view;
        this.musicDataSource = musicDataSource;
        userId = user.getId();
    }

    @Override
    public void saveClicked(List<String> selectedGenres) {
        view.showProgressBar(R.string.progress_saving_genres);
        musicDataSource.setFavoriteGenres(userId, selectedGenres);
        view.dismissProgressDialog();
        view.goNextScreen();
    }

}
