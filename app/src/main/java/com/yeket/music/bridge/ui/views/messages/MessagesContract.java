package com.yeket.music.bridge.ui.views.messages;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

public interface MessagesContract {

    String SENDER_ID_KEY = "sender_id";

    interface View{
        void addMessages(List<IMessage> messages);
        void addMessage(IMessage message);
        void setInputListener(MessageInput.InputListener listener);
        void setOnLoadMoreListener(MessagesListAdapter.OnLoadMoreListener listener);
        void setSelectionListener(MessagesListAdapter.SelectionListener listener);
        void setAttachmentsListener(MessageInput.AttachmentsListener listener);
        void unselectItems();
        void goBack();
        void showError(int messageResourceId);
        void showProgressBar(int messageResourceId);
        void dismissProgressDialog();
    }

    interface Presenter{
        void start();
        void stop();
        void backPressed();
    }

}
