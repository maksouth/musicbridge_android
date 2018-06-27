package com.yeket.music.bridge.datasource.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.models.chat.Dialog;
import com.yeket.music.bridge.models.chat.Message;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DialogDataSource {

    /**
     * subscribe to new dialog event for current user
     * also set listener for new message in the all dialogs
     * @param newDialogsListener - receiver to return new dialog object
     * @param lastMessageUpdateListener - receiver to new messages in all dialogs
     */
    void subscribeOnDialogsUpdates(@NonNull String userId,
                                   @NonNull SuccessCallback<Dialog> newDialogsListener,
                                   @NonNull SuccessCallback<Dialog> lastMessageUpdateListener);

    /**
     * un-subscribe from new dialogs creation, dialog update events
     */
    void unsubscribeFromDialogsUpdates();

    /**
     * get all dialogs with user with currentUserId asynchronously
     * add runOnUIThread in listener if you want to update ui with this data
     * @param listener
     */
    void getDialogs(@NonNull String userId,
                    @NotNull SuccessCallback<List<Dialog>> listener,
                    @Nullable FailureCallback failureCallback);

    /**
     * Find dialog id asynchronously
     * for dialog where 2 given users present
     * @param firstUserId - id of the first user
     * @param secondUserId - id of the second user
     * @param listener - receiver to return dialog id asynchronously
     */
    void getDialogId(String firstUserId,
                     String secondUserId,
                     @NotNull SuccessCallback<String> listener,
                     @Nullable FailureCallback failureCallback);

    void decreaseUnreadCount(String dialogId, String userId);

    void updateLastDialogMessage(String dialogId, final Message message);

    void increaseUnreadCount(String senderId, String dialogId);

    void checkAndMarkLastMessageAsRead(String dialogId, String messageId);

}
