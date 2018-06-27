package com.yeket.music.bridge.ui.views.messages;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.yeket.music.R;
import com.yeket.music.bridge.infrastructure.di.components.DaggerMessagesPresenterComponent;
import com.yeket.music.bridge.infrastructure.di.modules.DialogIdModule;
import com.yeket.music.bridge.infrastructure.di.modules.ViewModule;
import com.yeket.music.bridge.ui.views.LuresApplication;

import java.util.List;

import static com.yeket.music.bridge.ui.views.dialogs.DialogListFragment.DIALOG_ID_KEY;
import static com.yeket.music.bridge.ui.views.messages.MessagesContract.SENDER_ID_KEY;


public class MessagesActivity extends AppCompatActivity
        implements MessagesContract.View{

    private MessagesContract.Presenter presenter;

    private MessageInput messageInput;
    private MessagesListAdapter<IMessage> messagesAdapter;
    private MessagesList messagesList;
    private ImageLoader imageLoader;

    private ProgressDialog progressDialog;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        String dialogId = getIntent().getStringExtra(DIALOG_ID_KEY);
        String senderId = getIntent().getStringExtra(SENDER_ID_KEY);

        imageLoader = (imageView, url) -> Picasso.with(MessagesActivity.this).load(url).into(imageView);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        messageInput = (MessageInput) findViewById(R.id.input);

        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);

//        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
//                new MessagesListAdapter.OnMessageViewClickListener<IMessage>() {
//                    @Override
//                    public void onMessageViewClick(View view, IMessage message) {
//                        Toast.makeText(MessagesActivity.this,
//                                        message.getUser().getName() + " avatar click",
//                                        Toast.LENGTH_SHORT)
//                                        .show();
//                    }
//                });
        messagesList.setAdapter(messagesAdapter);

        presenter = DaggerMessagesPresenterComponent
                .builder()
                .dialogIdModule(new DialogIdModule(dialogId))
                .viewModule(new ViewModule(this))
                .singletonComponent(((LuresApplication)getApplication()).getSingletonComponent())
                .build()
                .getMessagesPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public void onBackPressed() {
        presenter.backPressed();
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    @Override
    public void showError(int messageResourceId) {
        Snackbar.make(rootLayout, getString(messageResourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(int messageResourceId) {
        if(progressDialog != null){
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(messageResourceId));
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        progressDialog = null;
    }

    @Override
    public void addMessages(List<IMessage> messages) {
        messagesAdapter.addToEnd(messages, true);
    }

    @Override
    public void addMessage(IMessage message) {
        messagesAdapter.addToStart(message, true);
    }

    @Override
    public void setInputListener(MessageInput.InputListener listener) {
        messageInput.setInputListener(listener);
    }

    @Override
    public void setOnLoadMoreListener(MessagesListAdapter.OnLoadMoreListener listener) {
        messagesAdapter.setLoadMoreListener(listener);
    }

    @Override
    public void setSelectionListener(MessagesListAdapter.SelectionListener listener) {
        messagesAdapter.enableSelectionMode(listener);
    }

    @Override
    public void setAttachmentsListener(MessageInput.AttachmentsListener listener) {
        messageInput.setAttachmentsListener(listener);
    }

    @Override
    public void unselectItems() {
        messagesAdapter.unselectAllItems();
    }
}
