package com.yeket.music.bridge.ui.views.messages;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yeket.music.R;
import com.yeket.music.bridge.datasource.chat.MessagesDataSource;
import com.yeket.music.bridge.infrastructure.UserHolder;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.models.chat.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class MessagesPresenter implements MessagesContract.Presenter,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener{

    private static final int TOTAL_MESSAGES_COUNT = Integer.MAX_VALUE;
    private static final int LOAD_MESSAGE_COUNT = 10;

    private MessagesDataSource messagesDataSource;
    private MessagesContract.View view;
    private User user;

    private String dialogId;
    private int selectionCount;

    @Inject
    public MessagesPresenter(MessagesContract.View view,
                             User user,
                             @Named("dialogId") String dialogId,
                             MessagesDataSource messagesDataSource){
        this.view = view;
        this.user = user;
        this.dialogId = dialogId;
        this.messagesDataSource = messagesDataSource;

        view.setSelectionListener(this);
        view.setOnLoadMoreListener(this);
        view.setInputListener(this);
        view.setAttachmentsListener(this);

        loadMessages();
    }

    @Override
    public void start(){
        messagesDataSource.subscribeOnMessages(user.getId(),
                dialogId, view::addMessage);
    }

    @Override
    public void stop() {
        messagesDataSource.unsubscribeFromMessages();
    }

    @Override
    public void backPressed() {
        if (selectionCount == 0) {
            view.goBack();
        } else {
            view.unselectItems();
        }
    }

    @Override
    public void onAddAttachments() {
        // TODO: 06.08.17 save to storage
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        messagesDataSource.createMessage(dialogId, input.toString(), user,
                null, LoggerFailureCallback.EMPTY);
        return true;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
    }

    private void loadMessages() {
        view.showProgressBar(R.string.progress_loading_messages);
        messagesDataSource.loadMessages(dialogId, LOAD_MESSAGE_COUNT,
                user.getId(),
                data -> {
                    List<IMessage> messages = new ArrayList<>(data);
                    view.addMessages(messages);
                    view.dismissProgressDialog();
                },
                (LoggerFailureCallback) errorResponse -> view.dismissProgressDialog());
    }
}
