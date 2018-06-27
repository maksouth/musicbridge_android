package com.yeket.music.bridge.datasource.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.models.chat.Message;
import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import java.util.List;

public interface MessagesDataSource {

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
    void createMessage(@NonNull String dialogId,
                       @NonNull String payload,
                       @NonNull User senderUser,
                       @Nullable CompleteCallback callback,
                       @Nullable FailureCallback failureCallback);

    /**
     * set listener to read old messages
     * ordered by creation time asynchronously
     * till current millisecond,
     * when older message is read cached time of oldest message is changed
     * so more latest or new messages will be rejected
     * @param dialogId - dialog for which read messages
     * @param limit - number of messages in portion
     * @param listener - receiver for asynchronous messages receiving
     */
    void loadMessages(@NonNull String dialogId,
                      int limit,
                      @NonNull String userId,
                      @NonNull SuccessCallback<List<Message>> listener,
                      @Nullable FailureCallback failureCallback);

    /**
     * subscribe on new messages
     * for specific dialog with dialogId
     * messages return asynchronously by listener
     *
     * this dialogId will then be associated with current dialog id
     * @param listener
     * @param dialogId
     */
    void subscribeOnMessages(@NonNull String userId,
                             @NonNull String dialogId,
                             @NonNull SuccessCallback<Message> listener);

    /**
     * un-subscribe from new messages
     * for current dialog
     */
    void unsubscribeFromMessages();

}
