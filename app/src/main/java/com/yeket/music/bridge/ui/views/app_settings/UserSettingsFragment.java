package com.yeket.music.bridge.ui.views.app_settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.yeket.music.R;
import com.yeket.music.bridge.datasource.settings.SettingsDataSource;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.views.LuresApplication;

public class UserSettingsFragment extends Fragment implements AppSettingsContract.View{

    public static final String TAG = UserSettingsFragment.class.getSimpleName();
    private AppSettingsContract.ViewModel viewModel;

    private Switch likeSwitch;
    private Switch messageSwitch;
    private Switch matchSwitch;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getActivity().getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(getActivity()))
                .build()
                .userSettingsViewModel();

        viewModel.loadValues();

        view.findViewById(R.id.save_app_settings_button).setOnClickListener((v)->viewModel.saveClicked(v));

        likeSwitch = ((Switch)view.findViewById(R.id.new_likes_switch));
        likeSwitch.setOnCheckedChangeListener(((buttonView, isChecked) ->
                viewModel.notificationSwitched(SettingsDataSource.NotificationType.NEW_LIKE, isChecked)));

        matchSwitch = ((Switch)view.findViewById(R.id.new_matches_switch));
        matchSwitch.setOnCheckedChangeListener(((buttonView, isChecked) ->
                viewModel.notificationSwitched(SettingsDataSource.NotificationType.MATCH, isChecked)));

        messageSwitch = ((Switch)view.findViewById(R.id.new_messages_switch));
        messageSwitch.setOnCheckedChangeListener(((buttonView, isChecked) ->
                viewModel.notificationSwitched(SettingsDataSource.NotificationType.NEW_MESSAGE, isChecked)));
    }

    @Override
    public void goNextScreen() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void setNewMessageNotification(boolean value) {
        messageSwitch.setChecked(value);
    }

    @Override
    public void setNewLikeNotification(boolean value) {
        likeSwitch.setChecked(value);
    }

    @Override
    public void setNewMatchNotification(boolean value) {
        matchSwitch.setChecked(value);
    }
}
