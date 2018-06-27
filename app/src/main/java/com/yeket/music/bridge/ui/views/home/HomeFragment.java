package com.yeket.music.bridge.ui.views.home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.components.PresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.PlayerViewModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.services.player.PlayerContract;
import com.yeket.music.bridge.ui.adapters.UserSwipeableCardAdapter;
import com.yeket.music.bridge.ui.views.BaseFragment;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

public class HomeFragment extends BaseFragment implements HomeContract.View,
        PlayerContract.View, View.OnClickListener{

    private HomeContract.Presenter presenter;
    private UserSwipeableCardAdapter adapter;

    private CardStackView cardStackView;

    private ConstraintLayout rootLayout;
    private Button playButton;
    private Button likeButton;
    private Button dislikeButton;
    private TextView trackLabel;
    private TextView artistLabel;

    private View overlayLayout;
    private TextView overlayNoUsersMsg;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initialize();
        initializeUI(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    private void initialize(){

        PresenterComponent component  = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getActivity().getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(getActivity()))
                .playerViewModule(new PlayerViewModule(this))
                .build();

        presenter = component.homePresenter();
        adapter = component.cardAdapter();

        presenter.start();
    }

    private void initializeUI(View view){
        rootLayout = (ConstraintLayout) view.findViewById(R.id.root_layout);
        overlayLayout = view.findViewById(R.id.layoutOverlay);
        overlayNoUsersMsg = (TextView) view.findViewById(R.id.text_view_overlay_message);
        overlayNoUsersMsg.setText(R.string.no_cards_to_display);
        showOverlay(false);


        artistLabel = (TextView) view.findViewById(R.id.artist_label);
        trackLabel = (TextView) view.findViewById(R.id.track_label);

        cardStackView = (CardStackView) view.findViewById(R.id.card_stack_view);

        cardStackView.setAdapter(adapter);

        playButton = (Button) view.findViewById(R.id.player_button);
        playButton.setOnClickListener(this);

        likeButton = (Button) view.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);

        dislikeButton = (Button) view.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);

        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {}

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());

                if(SwipeDirection.Left.equals(direction)){
                    presenter.cardSwipeDislike();
                } else {
                   presenter.cardSwipeLike();
                }

            }

            @Override
            public void onCardReversed() {}

            @Override
            public void onCardMovedToOrigin() {}

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });

    }

    @Override
    public void showOverlay(boolean showMessage) {
        overlayLayout.bringToFront();
        overlayLayout.setVisibility(View.VISIBLE);
        if(showMessage)
            overlayNoUsersMsg.setVisibility(View.VISIBLE);
        else
            overlayNoUsersMsg.setVisibility(View.INVISIBLE);

    }

    @Override
    public void hideOverlay() {
        overlayLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addData(final LuresUser user) {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            getActivity().runOnUiThread(() -> {
                adapter.add(user);
                adapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void changePlayerButton(boolean isPlayMode) {

        if(isPlayMode) {
            playButton.setBackground(getResources().getDrawable(R.drawable.pause_button));
        }else {
            playButton.setBackground(getResources().getDrawable(R.drawable.play_button));
        }

    }

    public void swipeLeft() {

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Left, set);
    }

    public void swipeRight() {

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Right, set);
    }

    @Override
    public void setTrackName(String trackName) {
        trackLabel.setText(trackName);
    }

    @Override
    public void setArtistName(String artistName) {
        artistLabel.setText(artistName);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.player_button:
                presenter.playerClicked();
                break;

            case R.id.like_button:
                presenter.likeButtonClick();
                break;

            case R.id.dislike_button:
                presenter.dislikeButtonClick();
                break;
        }

    }

    @Override
    protected ViewGroup getRootLayout() {
        return rootLayout;
    }
}
