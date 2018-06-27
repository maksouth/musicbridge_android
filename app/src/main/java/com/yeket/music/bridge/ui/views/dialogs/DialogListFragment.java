package com.yeket.music.bridge.ui.views.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.ContextModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.models.chat.Dialog;
import com.yeket.music.bridge.ui.views.BaseFragment;
import com.yeket.music.bridge.ui.views.LuresApplication;
import com.yeket.music.bridge.ui.views.messages.MessagesActivity;

import java.util.List;

import static com.yeket.music.bridge.ui.views.messages.MessagesContract.SENDER_ID_KEY;

public class DialogListFragment extends BaseFragment implements DialogsListAdapter.OnDialogClickListener<Dialog>,
                    DialogListContract.View{

    public static final String TAG = DialogListFragment.class.getSimpleName();

    public static final String DIALOG_ID_KEY = "dialog_id";

    private DialogsListAdapter<Dialog> dialogsListAdapter;
    private DialogsList dialogsList;
    private ImageLoader imageLoader;
    private ConstraintLayout rootLayout;

    private View overlayLayout;
    private TextView overlayNoUsersMsg;

    private DialogListContract.Presenter presenter;

    public DialogListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        overlayLayout = view.findViewById(R.id.layoutOverlay);
        overlayNoUsersMsg = (TextView) view.findViewById(R.id.text_view_overlay_message);
        overlayNoUsersMsg.setText(R.string.you_have_no_dialogs_yet);
        showOverlay(false);

        rootLayout = (ConstraintLayout) view.findViewById(R.id.root_layout);

        dialogsList = (DialogsList) view.findViewById(R.id.dialogsList);

        imageLoader = (imageView, url) -> Picasso.with(getContext()).load(url).into(imageView);
        dialogsListAdapter = new DialogsListAdapter(imageLoader);
        dialogsListAdapter.setOnDialogClickListener(this);
        dialogsList.setAdapter(dialogsListAdapter, false);

        presenter = DaggerPresenterComponent
                .builder()
                .singletonComponent(((LuresApplication)getActivity().getApplication()).getSingletonComponent())
                .viewModule(new ViewModule(this))
                .contextModule(new ContextModule(getActivity()))
                .build()
                .dialogListPresenter();

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void showOverlay(final boolean showMessage) {
        if(getActivity()!= null && !getActivity().isDestroyed()){
            getActivity().runOnUiThread(() -> {
                overlayLayout.bringToFront();
                overlayLayout.setVisibility(View.VISIBLE);
                if(showMessage)
                    overlayNoUsersMsg.setVisibility(View.VISIBLE);
                else
                    overlayNoUsersMsg.setVisibility(View.INVISIBLE);
            });
        }
    }

    @Override
    public void hideOverlay() {
        if(getActivity()!= null && !getActivity().isDestroyed()){
            getActivity().runOnUiThread(() -> overlayLayout.setVisibility(View.INVISIBLE));
        }
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        presenter.dialogClick(dialog);
    }

    @Override
    public void addDialog(final Dialog dialog) {
        if(!getActivity().isDestroyed()) {
            getActivity().runOnUiThread(() -> dialogsListAdapter.addItem(0, dialog));

        }
    }

    @Override
    public void addDialogs(final List<Dialog> dialogs) {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            getActivity().runOnUiThread(() -> dialogsListAdapter.setItems(dialogs));
        }
    }

    @Override
    public void updateLastMessage(final Dialog dialog) {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            getActivity().runOnUiThread(() -> dialogsListAdapter.updateItemById(dialog));
        }
    }

    @Override
    public void goToDialogScreen(String dialogId, String senderId) {
        Intent messageActivityIntent = new Intent(getContext(), MessagesActivity.class);
        messageActivityIntent.putExtra(DIALOG_ID_KEY, dialogId);
        messageActivityIntent.putExtra(SENDER_ID_KEY, senderId);
        startActivity(messageActivityIntent);
    }

    @Override
    protected ViewGroup getRootLayout() {
        return rootLayout;
    }
}