package com.yeket.music.bridge.datasource.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.models.IUser;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.util.Checker;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.models.chat.Dialog;
import com.yeket.music.bridge.models.chat.Message;
import com.yeket.music.bridge.models.error.ErrorResponse;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.services.ChatUserManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import static com.yeket.music.bridge.datasource.util.Checker.extract;

public class FirebaseDialogDataSource implements DialogDataSource {

    private static final String TAG = FirebaseDialogDataSource.class.getSimpleName();

    private ChatUserManager chatUserManager;

    private Set<String> loadedDialogIdSet;

    private DatabaseReference mDialogsRef;

    /*
     * Firebase listener
     * for the new dialogs with user
     * and existing dialogs with user updates
     */
    private ChildEventListener dialogsChildListener;

    /*
     * Internal app listener
     * for new dialogs with user
     */
    private SuccessCallback<Dialog> newDialogsListener;
    /*
     * Internal listener
     * this listener updates dialog in dialog list
     * when dialog is updated (like new message -> last message update)
     */
    private SuccessCallback<Dialog> lastMessageUpdateListener;

    private long dialogInfoUpdateTime;

    private MessageParser messageParser;

    @Inject
    public FirebaseDialogDataSource(ChatUserManager chatUserManager, MessageParser messageParser){

        this.chatUserManager = chatUserManager;
        this.messageParser = messageParser;

        mDialogsRef = FirebaseDatabase.getInstance()
                .getReference(Schemes.Chat.CHAT_NODE_NAME)
                .child(Schemes.Chat.DIALOGS);

        mDialogsRef.keepSynced(true);

        loadedDialogIdSet = new ArraySet<>();

    }

    /**
     * get all dialogs with user with currentUserId asynchronously
     * add runOnUIThread in listener if you want to update ui with this data
     * @param listener
     */
    @Override
    public void getDialogs(@NonNull String userId,
                           @NonNull SuccessCallback<List<Dialog>> listener,
                           @Nullable FailureCallback failureCallback) {
        Query query = mDialogsRef.orderByChild(Schemes.Chat.MEMBERS + "/" + userId).startAt(0);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                new Thread(() -> {

                    List<Dialog> dialogs = new ArrayList<>();
                    Iterator<DataSnapshot> dialogsSnapshotIterator = dataSnapshot.getChildren().iterator();
                    DataSnapshot dialogSnapshot;
                    Dialog tempDialog;

                    while (dialogsSnapshotIterator.hasNext()){

                        dialogSnapshot = dialogsSnapshotIterator.next();
                        tempDialog = readDialogMetadataFromSnapshot(userId, dialogSnapshot);

                        if(tempDialog.getLastMessage() != null) {

                            loadedDialogIdSet.add(tempDialog.getId());
                            dialogs.add(tempDialog);

                        }

                    }

                    listener.onSuccess(dialogs);

                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(failureCallback != null) {
                    failureCallback.onFailure(Errors.GET_DIALOGS_CANCELLED);
                }
            }
        });

    }

    /**
     * Find dialog id asynchronously
     * for dialog where 2 given users present
     * @param firstUserId - id of the first user
     * @param secondUserId - id of the second user
     * @param successCallback - callback for success response with dialog Id
     * @param failureCallback - callback for failure response while getting dialog id
     */
    @Override
    public void getDialogId(String firstUserId, String secondUserId,
                            @NotNull SuccessCallback<String> successCallback,
                            @Nullable FailureCallback failureCallback) {

        Query query = mDialogsRef.orderByChild(Schemes.Chat.MEMBERS + "/" + firstUserId).startAt(0);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String dialogId = null;

                for(DataSnapshot dialogSnapshot : dataSnapshot.getChildren()){
                    if(dialogSnapshot.child(Schemes.Chat.MEMBERS + "/" + secondUserId).exists()){
                        dialogId = dialogSnapshot.getKey();
                        break;
                    }
                }

                if(dialogId != null){
                    successCallback.onSuccess(dialogId);
                } else {
                    if (failureCallback != null) {
                        failureCallback.onFailure(new ErrorResponse(Errors.GET_COUPLE_DIALOG_NOT_EXISTS, firstUserId, secondUserId));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (failureCallback != null) {
                    failureCallback.onFailure(new ErrorResponse(Errors.GET_COUPLE_DIALOG_CANCELLED, firstUserId, secondUserId));
                }
            }
        });
    }

    /**
     * subscribe to new dialog event for current user
     * also set listener for new message in the all dialogs
     * @param newDialogsListener - receiver to return new dialog object
     * @param lastMessageUpdateListener - receiver to new messages in all dialogs
     */
    @Override
    public void subscribeOnDialogsUpdates(@NonNull String userId,
                                          @NonNull SuccessCallback<Dialog> newDialogsListener,
                                          @NonNull SuccessCallback<Dialog> lastMessageUpdateListener) {

        initializeListeners(userId);

        if(dialogInfoUpdateTime == 0){
            dialogInfoUpdateTime = new Date().getTime();
        }
        this.newDialogsListener = newDialogsListener;
        this.lastMessageUpdateListener = lastMessageUpdateListener;

        mDialogsRef.orderByChild(Schemes.Chat.MEMBERS + "/" + userId)
                .startAt(0)
                .addChildEventListener(dialogsChildListener);

    }

    /**
     * un-subscribe from new dialogs creation, dialog update events
     */
    @Override
    public void unsubscribeFromDialogsUpdates() {
        newDialogsListener = null;
        lastMessageUpdateListener = null;
        mDialogsRef.removeEventListener(dialogsChildListener);
    }

    @Override
    public void increaseUnreadCount(String senderId, String dialogId){

        mDialogsRef.child(dialogId)
                .child(Schemes.Chat.MEMBERS)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        Iterator<MutableData> userSnapshotIterator = mutableData.getChildren().iterator();
                        MutableData userSnapshot;
                        String userId;
                        Integer unreadCount;

                        while (userSnapshotIterator.hasNext()){

                            userSnapshot = userSnapshotIterator.next();
                            userId = userSnapshot.getKey();

                            if (!senderId.equals(userId)) {
                                unreadCount = extract(userSnapshot.getValue(), Default.Chat.UNREAD_MESSAGES_COUNT);
                                userSnapshot.setValue(unreadCount+1);
                            }

                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });

    }

    @Override
    public void decreaseUnreadCount(String dialogId, String userId){
        mDialogsRef.child(dialogId)
                .child(Schemes.Chat.MEMBERS)
                .child(userId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        Integer unreadCount = extract(mutableData.getValue(), Default.Chat.UNREAD_MESSAGES_COUNT);

                        if (unreadCount > 0) {
                            mutableData.setValue(unreadCount - 1);
                            return Transaction.success(mutableData);
                        }

                        return Transaction.abort();
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    @Override
    public void updateLastDialogMessage(String dialogId, final Message message){
        final DatabaseReference lastMessageRef;

        if(dialogId != null){
            lastMessageRef = mDialogsRef.child(dialogId).child(Schemes.Chat.LAST_MESSAGE);
            lastMessageRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    Long lastMessageCreatedAt = extract(mutableData.child(Schemes.Chat.TIMESTAMP).getValue(),
                            new Date().getTime());

                    if(lastMessageCreatedAt < message.getCreatedAt().getTime()) {

                        mutableData.child(Schemes.Chat.MESSAGE_ID).setValue(message.getId());
                        mutableData.child(Schemes.Chat.PAYLOAD).setValue(message.getText());
                        mutableData.child(Schemes.Chat.IS_READ).setValue(message.isRead());
                        mutableData.child(Schemes.Chat.SENDER_ID).setValue(message.getUser().getId());
                        mutableData.child(Schemes.Chat.TIMESTAMP).setValue(message.getCreatedAt().getTime());

                    }

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    updateDialogLastUpdateTime(dialogId);
                }
            });
        }
    }

    @Override
    public void checkAndMarkLastMessageAsRead(String dialogId, String messageId){
        mDialogsRef.child(dialogId)
                .child(Schemes.Chat.LAST_MESSAGE)
                .child(Schemes.Chat.MESSAGE_ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentDialogLastMessageId = extract(dataSnapshot.getValue(), Default.Chat.LAST_MESSAGE_ID);

                        if (currentDialogLastMessageId.equals(messageId)) {

                            mDialogsRef.child(dialogId)
                                    .child(Schemes.Chat.LAST_MESSAGE)
                                    .child(Schemes.Chat.IS_READ)
                                    .setValue(true);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initializeListeners(String userId){

        /**
         * listener is set for node with dialogs
         * and checks for new dialogs with user_id same to current_user_id
         *
         */
        dialogsChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                if( newDialogsListener != null ) {

                    new Thread(() -> {

                        Dialog dialog;
                        Object lastUpdateTimeObject = dataSnapshot.child(Schemes.Chat.LAST_UPDATE_TIME).getValue();
                        long lastUpdateTime = Checker.extract(lastUpdateTimeObject, new Date().getTime());

                        /**
                         * check if this change was after the time dialog info was updated on the client
                         * because otherwise this can be just loading all dialogs from database
                         * This is like this listener works.
                         * When you launch the app and set this listener,
                         * it will invoke for all dialogs in database, not just new
                         *
                         * Cases:
                         * - dialog was created long time ago - do not process it (it's update time is old)
                         * - dialog is created after the last dialog was read from remote database - show it in list
                         */
                        if(lastUpdateTime > dialogInfoUpdateTime){

                            dialogInfoUpdateTime = lastUpdateTime;
                            dialog = readDialogMetadataFromSnapshot(userId, dataSnapshot);

                            if( dialog.getLastMessage() != null ) {
                                // TODO: 29.11.17 WHY DO WE NEED THIS CONDITION??
                                // won't be onChildChanged invoked??
                                if (loadedDialogIdSet.contains(dialog.getId())) {

                                    if (lastMessageUpdateListener != null) {
                                        lastMessageUpdateListener.onSuccess(dialog);
                                    }

                                } else {
                                    newDialogsListener.onSuccess(dialog);
                                }

                                loadedDialogIdSet.add(dialog.getId());
                            }
                        }

                    }).start();

                }
            }

            @Override
            public void onChildChanged(final DataSnapshot dataSnapshot, String s) {

                /**
                 * in this case dialog was already read,
                 * and is updated after all dialogs are read,
                 * or since last dialog update
                 */
                if( lastMessageUpdateListener != null ){

                    new Thread(() -> {

                        Dialog dialog;
                        long lastUpdateTime = extract(dataSnapshot.child(Schemes.Chat.LAST_UPDATE_TIME).getValue(),
                                new Date().getTime());

                        if(lastUpdateTime > dialogInfoUpdateTime){
                            dialogInfoUpdateTime = lastUpdateTime;
                            dialog = readDialogMetadataFromSnapshot(userId, dataSnapshot);

                            if(dialog.getLastMessage()!=null) {

                                loadedDialogIdSet.add(dialog.getId());
                                lastMessageUpdateListener.onSuccess(dialog);

                            }

                        }

                    }).start();

                }
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

    private Dialog readDialogMetadataFromSnapshot(@NonNull String userId,
                                                  @NonNull DataSnapshot dialogSnapshot){

        // TODO: 11.08.17 add checks for null

        Dialog dialog = new Dialog();
        DataSnapshot membersSnapshot = dialogSnapshot.child(Schemes.Chat.MEMBERS);
        Iterator<DataSnapshot> membersSnapshotIterator = membersSnapshot.getChildren().iterator();
        DataSnapshot memberSnapshot;
        final List<IUser> members = new ArrayList<>();
        int currentUserUnreadCount = 0;
        final Semaphore semaphore = new Semaphore(0);

        while (membersSnapshotIterator.hasNext()) {
            memberSnapshot = membersSnapshotIterator.next();

            if (userId.equals(memberSnapshot.getKey())) {
                currentUserUnreadCount = extract(memberSnapshot.getValue(), Default.Chat.UNREAD_MESSAGES_COUNT);
            }

            chatUserManager.getUser(memberSnapshot.getKey(),
                    data -> {
                        members.add(data);
                        semaphore.release();
                    },
                    (LoggerFailureCallback) errorResponse -> semaphore.release());

            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: 12.08.17 RxJava
        }

        dialog.setUsers(members);
        dialog.setName(selectDialogName(userId, members));
        dialog.setPhoto(selectChatAvatar(userId, members));
        dialog.setId(dialogSnapshot.getKey());
        dialog.setUnreadCounts(currentUserUnreadCount);
        dialog.setLastMessage(messageParser.extract(dialogSnapshot.child(Schemes.Chat.LAST_MESSAGE)));

        Log.d(TAG, "Dialog id: " + dialog.getId());
        Log.d(TAG, "Dialog ids: " + loadedDialogIdSet);

        return dialog;

    }

    private String selectDialogName(String userId, List<IUser> members){

        StringBuilder name = new StringBuilder();

        for(IUser user : members){

            if (!userId.equals(user.getId())) {
                name.append(user.getName()).append(" ");
            }

        }

        return name.toString();

    }

    private String selectChatAvatar(String userId, List<IUser> members){

        for(IUser user : members){

            if( !userId.equals( user.getId() ) ){
                return user.getAvatar();
            }

        }

        return members.get(0).getAvatar();

    }

    private void updateDialogLastUpdateTime(String dialogId){

        mDialogsRef.child(dialogId)
                .child(Schemes.Chat.LAST_UPDATE_TIME)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        mutableData.setValue(new Date().getTime());

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });

    }

}
