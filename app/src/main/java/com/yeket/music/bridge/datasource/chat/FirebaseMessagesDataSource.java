package com.yeket.music.bridge.datasource.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.models.chat.Message;
import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.services.ChatUserManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import static com.yeket.music.bridge.datasource.util.Checker.extract;

public class FirebaseMessagesDataSource implements MessagesDataSource{

    // use only for load old messages
    // for receiving new messages use start time new Date().getTime()
    // time of the oldest message loaded by now
    private long lastMessageLoaded = 0;

    /*
     * Firebase listener
     * for new message in specific dialog
     */
    private ChildEventListener dialogMessagesChildListener;
    /*
     * Internal listener
     * for new message in specific dialog
     */
    private SuccessCallback<Message> dialogMessageListener;

    private DatabaseReference mMessagesRef;
    private DatabaseReference dialogRef;

    private ChatUserManager chatUserManager;
    private DialogDataSource dialogDataSource;
    private MessageParser messageParser;

    @Inject
    public FirebaseMessagesDataSource(@NonNull ChatUserManager chatUserManager,
                                      @NonNull DialogDataSource dialogDataSource,
                                      @NonNull MessageParser messageParser){

        this.chatUserManager = chatUserManager;
        this.dialogDataSource = dialogDataSource;
        this.messageParser = messageParser;

        mMessagesRef = FirebaseDatabase.getInstance()
                .getReference(Schemes.Chat.CHAT_NODE_NAME)
                .child(Schemes.Chat.MESSAGES);

        mMessagesRef.keepSynced(true);
    }

    /**
     * set listener to read old messages
     * ordered by creation time asynchronously
     * till current millisecond,
     * when older message is read cached time of oldest message is changed
     * so more latest or new messages will be rejected
     * @param dialogId - dialog for which read messages
     * @param limit - number of messages in portion
     * @param callback - callback for async success response with message
     * @param failureCallback - callback for async failure response while loading messages
     */
    @Override
    public void loadMessages(@NonNull String dialogId,
                             int limit,
                             @NonNull String userId,
                             @NonNull SuccessCallback<List<Message>> callback,
                             @Nullable FailureCallback failureCallback) {

        if(dialogId == null){
            failureCallback.onFailure(Errors.LOAD_MESSAGES_DIALOG_ID_NULL);
            return;
        }

        /*
         set timestamp of the oldest message received.
         timestamp of the next message will be compared with given to check if it is
         older message
         */
        if(lastMessageLoaded == 0 || dialogRef == null || !dialogRef.getKey().equals(dialogId)){
            lastMessageLoaded = new Date().getTime();
            dialogRef = mMessagesRef.child(dialogId);
        }

        Query query  = dialogRef.orderByChild(Schemes.Chat.TIMESTAMP)
                .endAt(lastMessageLoaded-1)
                .limitToLast(limit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    List<Message> messages = new ArrayList<>();
                    Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
                    Message tempMessage;

                    while (children.hasNext()) {
                        tempMessage = messageParser.extract(children.next());

                        markAsRead(dialogId, tempMessage, userId);

                        if (tempMessage.getCreatedAt().getTime() < lastMessageLoaded) {
                            lastMessageLoaded = tempMessage.getCreatedAt().getTime();
                        }
                        messages.add(tempMessage);
                    }
                    callback.onSuccess(messages);
                } else {
                    if (failureCallback != null)
                        failureCallback.onFailure(Errors.LOAD_MESSAGES_NOT_EXIST);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (failureCallback != null)
                    failureCallback.onFailure(Errors.LOAD_MESSAGES_CANCELLED);
            }
        });
    }

    /**
     * Create new message in Firebase remote database as a transaction
     * for specific dialog with dialog id
     * with payload, current time and user id of current user
     * set this message as not read yet.
     *
     * If message is created successfully,
     * update dialog info (last update time, last message)
     * and update unread count for each user in dialog, except the current user
     *
     * @param dialogId - dialog in which message should be created
     * @param payload - text of the message
     * @param callback - is invoked when message is created successfully
     * @param failureCallback - is invoked when error occurs while creating message
     */
    @Override
    public void createMessage(@NonNull final String dialogId,
                              @NonNull final String payload,
                              @NonNull User senderUser,
                              @Nullable CompleteCallback callback,
                              @Nullable FailureCallback failureCallback) {

        DatabaseReference newMessageRef = mMessagesRef.child(dialogId).push();
        final String messageId = newMessageRef.getKey();
        final Date createdAt = new Date();

        newMessageRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                mutableData.child(Schemes.Chat.MESSAGE_ID).setValue(messageId);
                mutableData.child(Schemes.Chat.SENDER_ID).setValue(senderUser.getId());
                mutableData.child(Schemes.Chat.TIMESTAMP).setValue(createdAt.getTime());
                mutableData.child(Schemes.Chat.PAYLOAD).setValue(payload);
                mutableData.child(Schemes.Chat.IS_READ).setValue(false);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                if(databaseError == null){

                    Message message = new Message();

                    message.setId(messageId);
                    message.setUser(senderUser);
                    message.setCreatedAt(createdAt);
                    message.setText(payload);
                    message.setRead(false);

                    dialogDataSource.updateLastDialogMessage(dialogId, message);

                    dialogDataSource.increaseUnreadCount(senderUser.getId(), dialogId);
                }

                if(databaseError == null){
                    if(callback != null)
                        callback.onComplete();
                } else if (failureCallback != null)
                    failureCallback.onFailure(Errors.CREATE_MESSAGE_FAIL);

            }
        });

    }

    /**
     * subscribe on new messages
     * for specific dialog with dialogId
     * messages return asynchronously by listener
     *
     * this dialogId will then be associated with current dialog id
     * @param callback - callback for async success response with message
     * @param dialogId
     */
    @Override
    public void subscribeOnMessages(@NonNull String userId,
                                    @NonNull String dialogId,
                                    @NonNull SuccessCallback<Message> callback) {

        //????
        if (dialogRef != null && dialogMessagesChildListener != null) {
            dialogRef.removeEventListener(dialogMessagesChildListener);
        }

        dialogRef = mMessagesRef.child(dialogId);

        initializeListener(dialogId, userId);
        dialogMessageListener = callback;
        dialogRef.orderByChild(Schemes.Chat.TIMESTAMP)
                .startAt(new Date().getTime())
                .addChildEventListener(dialogMessagesChildListener);

    }

    /**
     * un-subscribe from new messages
     * for current dialog
     */
    @Override
    public void unsubscribeFromMessages() {
        if (dialogRef != null && dialogMessagesChildListener != null)
            dialogRef.removeEventListener(dialogMessagesChildListener);
        dialogRef = null;
    }

    private void markAsRead(String dialogId, Message message, String userId){

        if( !message.isRead() && !userId.equals( message.getUser().getId() ) ){

            dialogRef.child(message.getId())
                    .child(Schemes.Chat.IS_READ)
                    .setValue(true);


            dialogDataSource.checkAndMarkLastMessageAsRead(dialogId, message.getId());
            dialogDataSource.decreaseUnreadCount(dialogId, userId);
        }
    }

//    private Message readMessageFromSnapshot(DataSnapshot messageSnapshot){
//
//        Message message = null;
//
//        if(messageSnapshot.exists()) {
//
//            message = new Message();
//
//            String userId = extract(messageSnapshot.child(Schemes.Chat.SENDER_ID).getValue(), Default.Chat.USER_ID);
//            User sender = chatUserManager.getCachedUser(userId);
//
//            long timestamp = extract(messageSnapshot.child(Schemes.Chat.TIMESTAMP).getValue(), getCurrentMillis());
//            String payload = extract(messageSnapshot.child(Schemes.Chat.PAYLOAD).getValue(), Default.Chat.MESSAGE_PAYLOAD);
//            String messageId = extract(messageSnapshot.child(Schemes.Chat.MESSAGE_ID).getValue(), Default.Chat.MESSAGE_ID);
//            boolean isRead = extract(messageSnapshot.child(Schemes.Chat.IS_READ).getValue(), Default.Chat.IS_READ);
//
//            message.setUser(sender);
//            message.setId(messageId);
//            message.setText(payload);
//            message.setRead(isRead);
//            message.setCreatedAt(new Date(timestamp));
//
//        }
//
//        return message;
//    }

    private long getCurrentMillis(){
        return new Date().getTime();
    }

    private void initializeListener(String dialogId, String userId){
        dialogMessagesChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = messageParser.extract(dataSnapshot);

                markAsRead(dialogId, message, userId);

                if(dialogMessageListener != null){
                    dialogMessageListener.onSuccess(message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

}
