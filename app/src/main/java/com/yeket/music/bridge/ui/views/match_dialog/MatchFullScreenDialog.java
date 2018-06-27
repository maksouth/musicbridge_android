package com.yeket.music.bridge.ui.views.match_dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yeket.music.R;
import com.yeket.music.bridge.datasource.chat.DialogDataSource;
import com.yeket.music.bridge.datasource.users.UsersDataSource;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.di.components.DaggerMatchFullScreenDialogComponent;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.messages.MessagesActivity;

import javax.inject.Inject;

import static com.yeket.music.bridge.ui.views.dialogs.DialogListFragment.DIALOG_ID_KEY;
import static com.yeket.music.bridge.ui.views.main.MainContract.CURRENT_USER_ID;
import static com.yeket.music.bridge.ui.views.main.MainContract.LIKED_USER_ID;
import static com.yeket.music.bridge.ui.views.messages.MessagesContract.SENDER_ID_KEY;

public class MatchFullScreenDialog extends DialogFragment implements View.OnClickListener{

    @Inject
    UsersDataSource usersDataSource;
    @Inject
    DialogDataSource dialogDataSource;
    @Inject
    LuresUser currentUser;

    private ImageView firstProfileImage;
    private ImageView secondProfileImage;
    private TextView messageLabel;
    private Button startChatButton;

    private Button laterButton;

    private String currentUserId;
    private String likedUserId;

    public MatchFullScreenDialog(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.DialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_it_is_match, parent, false);

        firstProfileImage = (ImageView) view.findViewById(R.id.first_profile_image);
        secondProfileImage = (ImageView) view.findViewById(R.id.second_profile_image);
        messageLabel = (TextView) view.findViewById(R.id.message_label);
        laterButton = (Button) view.findViewById(R.id.later_button);
        startChatButton = (Button) view.findViewById(R.id.start_chating_button);

        laterButton.setOnClickListener(this);
        startChatButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DaggerMatchFullScreenDialogComponent
                .builder()
                .singletonComponent(((LuresApplication)getActivity().getApplication()).getSingletonComponent())
                .build()
                .inject(this);

        Bundle bundle = getArguments();
        currentUserId = bundle.getString(CURRENT_USER_ID);
        likedUserId = bundle.getString(LIKED_USER_ID);

        loadUserData(likedUserId, firstProfileImage);
        loadUserData(currentUserId, secondProfileImage);

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if(dialog.getWindow() != null)
                dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.later_button:
                getDialog().dismiss();
                break;

            case R.id.start_chating_button:

                dialogDataSource.getDialogId(currentUserId, likedUserId, dialogId -> {

                            Intent messagesIntent
                                    = new Intent(getActivity().getApplicationContext(), MessagesActivity.class);

                            messagesIntent.putExtra(SENDER_ID_KEY, currentUserId);
                            messagesIntent.putExtra(DIALOG_ID_KEY, dialogId);

                            startActivity(messagesIntent);
                            getDialog().dismiss();
                        },
                        //must be sure that main activity is on dialogs fragment
                        (LoggerFailureCallback) error -> getDialog().dismiss());
                break;

        }
    }

    private void loadUserData(final String userId, final ImageView imageView){

        usersDataSource.getUser(userId,
                user -> onGetUserSuccess(user, imageView),
                LoggerFailureCallback.EMPTY);
    }

    private void onGetUserSuccess(LuresUser user, ImageView imageView){
        Picasso.with(getActivity().getApplicationContext())
                .load(user.getImageUri())
                .into(imageView);

        if (!currentUser.getId().equals(user.getId())) {
            String userName = getName(user.getDisplayName());
            messageLabel.setText(getActivity().getString(R.string.you_and_user_matched, userName));
        }
    }

    private String getName(String original){
        return original.substring(0, original.indexOf(" "));
    }
}
