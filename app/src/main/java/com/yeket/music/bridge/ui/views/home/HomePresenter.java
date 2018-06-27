package com.yeket.music.bridge.ui.views.home;

import android.util.Log;

import com.yeket.music.R;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.likes.LikesDataSource;
import com.yeket.music.bridge.datasource.users.RecommendationsDataSource;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.services.player.Player;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomePresenter implements HomeContract.Presenter {

    private static final int SHOW_ARTIST_LIMIT = 5;

    private HomeContract.View view;

    private LikesDataSource likesDataSource;
    private RecommendationsDataSource recommendationsDataSource;

    private Player player;

    private List<LuresUser> users;

    private LuresUser currentUser;

    @Inject
    public HomePresenter(HomeContract.View view,
                         LuresUser user,
                         LikesDataSource likesDataSource,
                         RecommendationsDataSource recommendationsDataSource,
                         Player player) {
        this.view = view;
        this.currentUser = user;
        users = new ArrayList<>();
        this.likesDataSource = likesDataSource;
        this.player = player;
        this.recommendationsDataSource = recommendationsDataSource;
    }

    @Override
    public void start() {

        recommendationsDataSource.getAllRecommendations(currentUser.getId(),
                user -> {
                    users.add(user);
                    view.addData(user);
                    view.dismissProgressDialog();
                    view.hideOverlay();
                },
                errorResponse -> {
                    if (Errors.GET_RECOMMENDATIONS_NOT_FOUND.equals(errorResponse)) {
                        view.dismissProgressDialog();
                        view.showOverlay(true);
                    } else view.showError(errorResponse.message);
                },
                () -> {
                    Log.d("HomePresenter", "Finished loading recommendations. Total users: " + users.size());
                    if (users.size() == 0) {
                        view.dismissProgressDialog();
                        view.showOverlay(true);
                    }
                });

        view.showProgressBar(R.string.progress_loading_users);
    }

    @Override
    public void pause() {
        player.stop();
    }

    @Override
    public void destroy() {
        player.release();
    }

    @Override
    public void cardSwipeLike() {

        likesDataSource.like(users.get(0).getId(), currentUser.getId());

        users.remove(0);

        if (users.size() > 0) {
            player.prepareNextUserTrack(users.get(0).getId());
        }
        updateViewState();
    }

    private void updateViewState() {
        if (users.size() == 0) {
            view.showOverlay(true);
        }
    }

    @Override
    public void cardClick(int position) {
        //view.goToUserProfile(users.get(0).getId());
    }

    @Override
    public void cardSwipeDislike() {

        likesDataSource.dislike(users.get(0).getId(), currentUser.getId());

        users.remove(0);

        if (users.size() > 0) {
            player.prepareNextUserTrack(users.get(0).getId());
        }
        updateViewState();
    }

    @Override
    public void likeButtonClick() {
        if (!users.isEmpty()) {
            view.swipeRight();
        }
    }

    @Override
    public void dislikeButtonClick() {
        if (!users.isEmpty()) {
            view.swipeLeft();
        }
    }

    @Override
    public void playerClicked() {

        if (users.size() > 0) {
            player.click(users.get(0).getId());
        }

    }

//    @Override
//    public void onSuccess(final List<LuresUser> userList) {
//
//        for(final LuresUser temp : userList){
//            final boolean isLast = temp.equals(userList.get(userList.size()-1));
//            isOk(temp, new OneFuckingListener() {
//                @Override
//                public void success() {
//                    users.add(temp);
//
//                    view.addData(temp);
//                    view.dismissProgressDialog();
//                    view.hideOverlay();
//                }
//
//                @Override
//                public void failure() {
//                    if(isLast){
//                        view.dismissProgressDialog();
//                        view.showOverlay(true);
//                    }
//                }
//            });
//
//        }
//    }

}
