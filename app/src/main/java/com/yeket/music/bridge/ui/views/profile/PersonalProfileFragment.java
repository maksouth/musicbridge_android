package com.yeket.music.bridge.ui.views.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.ui_components.SizeConverter;
import com.yeket.music.bridge.ui.ui_components.TagTextView;
import com.yeket.music.bridge.ui.views.BaseFragment;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.app_settings.UserSettingsFragment;
import com.yeket.music.bridge.ui.views.details.DetailsActivity;
import com.yeket.music.bridge.ui.views.login.LoginActivity;
import com.yeket.music.bridge.ui.views.select_genres.SelectGenresFragment;

import java.util.ArrayList;
import java.util.List;

import static com.yeket.music.bridge.ui.views.details.DetailsActivity.EDIT_PROFILE_MODE;
import static com.yeket.music.bridge.ui.views.details.DetailsActivity.PRESENTATION_MODE_KEY;

public class PersonalProfileFragment extends BaseFragment implements PersonalProfileContract.View{

    public static final String GENRES = "genres";
    public static final String FAVORITE_GENRES = "favorite_genres";

    private PersonalProfileContract.Presenter presenter;

    ImageView profileImage;
    TextView nameLabel;
    TextView ageLabel;
    TextView noGenresLabel;
    LinearLayout genreTagsList;
    ConstraintLayout rootLayout;

    public PersonalProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.name, container, false);

        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getActivity().getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(getActivity()))
                .build()
                .personalProfilePresenter();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.logout_button).setOnClickListener(v -> presenter.logoutButtonClicked());
        view.findViewById(R.id.edit_button).setOnClickListener(v -> presenter.editButtonClicked());
        view.findViewById(R.id.edit_genres_button).setOnClickListener(v -> presenter.editGenresButtonClicked());
        view.findViewById(R.id.app_settings_button).setOnClickListener(v -> presenter.editAppSettingsClicked());

        profileImage = view.findViewById(R.id.profile_image);
        nameLabel = view.findViewById(R.id.name_label);
        ageLabel = view.findViewById(R.id.age_label);
        noGenresLabel = view.findViewById(R.id.no_genres_label);
        genreTagsList = view.findViewById(R.id.genre_tags_list);
        rootLayout = view.findViewById(R.id.root_layout);

        presenter.start();
    }

    @Override
    public void setImage(final String url) {
        showProgressBar(R.string.progress_loading_profile);

        Picasso.with(getActivity().getApplicationContext())
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(profileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getActivity().getApplicationContext())
                                .load(url)
                                .into(profileImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        dismissProgressDialog();
                                    }

                                    @Override
                                    public void onError() {
                                        dismissProgressDialog();
                                        showError(R.string.error_cannot_show_image);
                                    }
                                });
            }
        });
    }

    @Override
    public void setName(String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setAge(String age) {
        ageLabel.setText(age);
    }

    @Override
    public void goToLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void goEditGenresScreen(List<String> genres, List<String> favoriteGenres){

        Bundle bundle = new Bundle();
        bundle.putCharSequenceArrayList(GENRES, new ArrayList<>(genres));
        bundle.putCharSequenceArrayList(FAVORITE_GENRES, new ArrayList<>(favoriteGenres));

        SelectGenresFragment fragment = new SelectGenresFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToEditProfileScreen() {
        Intent detailsScreenIntent = new Intent(getActivity(), DetailsActivity.class);
        detailsScreenIntent.putExtra(PRESENTATION_MODE_KEY, EDIT_PROFILE_MODE);
        startActivity(detailsScreenIntent);
    }

    @Override
    public void goEditAppSettingsScreen() {
        UserSettingsFragment fragment = new UserSettingsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showGenres(List<String> genres) {

        if(genres.isEmpty()){
            noGenresLabel.setVisibility(View.VISIBLE);
            return;
        } else {
            noGenresLabel.setVisibility(View.GONE);
        }


        for(int i = 0; i < genres.size(); i++){
            final int finalI = i;
            TagTextView tagLabel = new TagTextView(getContext());
            tagLabel.post(() -> tagLabel.setText(genres.get(finalI)));

            int rightMargin = SizeConverter.toPx(8, getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, rightMargin, 0);
            tagLabel.setLayoutParams(params);

            genreTagsList.addView(tagLabel);
        }
    }


    @Override
    public ViewGroup getRootLayout() {
        return rootLayout;
    }
}
