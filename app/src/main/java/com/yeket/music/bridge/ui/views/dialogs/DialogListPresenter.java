package com.yeket.music.bridge.ui.views.dialogs;

import com.yeket.music.R;
import com.yeket.music.bridge.datasource.chat.DialogDataSource;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.chat.Dialog;
import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.models.error.ErrorResponse;

import java.util.List;

import javax.inject.Inject;

public class DialogListPresenter implements DialogListContract.Presenter {

    private DialogDataSource dialogDataSource;
    private SuccessCallback<Dialog> newDialogListener;
    private SuccessCallback<Dialog> lastMessageUpdateListener;

    private DialogListContract.View view;

    private User currentUser;

    @Inject
    public DialogListPresenter(DialogListContract.View view,
                               User user,
                               DialogDataSource dialogDataSource) {

        this.view = view;
        this.dialogDataSource = dialogDataSource;
        this.currentUser = user;

        // TODO: 06.01.18 move logic to start method, not in constructor
        view.showProgressBar(R.string.progress_loading_dialogs);
        dialogDataSource.getDialogs(currentUser.getId(), this::onGetDialogs, this::onGetFailure);
    }

    @Override
    public void start() {

        newDialogListener = dialog -> {
            view.hideOverlay();
            view.addDialog(dialog);
        };

        lastMessageUpdateListener = view::updateLastMessage;

        dialogDataSource.subscribeOnDialogsUpdates(currentUser.getId(), newDialogListener, lastMessageUpdateListener);
    }

    @Override
    public void stop() {
        newDialogListener = null;
        lastMessageUpdateListener = null;
        dialogDataSource.unsubscribeFromDialogsUpdates();
    }

    @Override
    public void dialogClick(Dialog dialog) {
        view.goToDialogScreen(dialog.getId(), currentUser.getId());
    }

    private void onGetFailure(ErrorResponse errorResponse){
        view.dismissProgressDialog();
        view.showError(errorResponse.message);
    }

    private void onGetDialogs(List<Dialog> dialogs){
        view.dismissProgressDialog();
        if (dialogs.size() == 0) {
            view.showOverlay(true);
        } else {
            view.hideOverlay();
            view.addDialogs(dialogs);
        }
    }

}
