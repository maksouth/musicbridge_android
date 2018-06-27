package com.yeket.music.bridge.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;

import javax.inject.Singleton;

/**
 * This class is responsible for:
 * - creating dialogs
 * - creating user for chat
 * - creating messages
 * - reading messages
 * - subscribing to messages updates
 * - updating dialog info (unread count for users, last update time) - should be moved to cloud functions
 * - subscribing to dialog updates
 * - subscribing to dialogs creation
 */
@Singleton //if needed
public interface ChatUserManager {

//    /**
//     * read currentUser from remote database
//     * or from local cache (if exists)
//     * and return in listener
//     * @param userId - id of current user
//     * @param listener - here user object will be returned
//     */
//    void loadCurrentUser(@NonNull String userId,
//                         @NonNull SuccessCallback<User> listener,
//                         @Nullable FailureCallback failureCallback);
//
//    /**
//     * get user synchronously from local cache
//     * this method should be called only
//     * after loadCurrentUser finished its asynchronous call
//     * @return current user object from local cache (if exists) or null
//     */
//    @Nullable
//    User getCurrentUser();

    /**
     * return User object by userId asynchronously
     * from local cache (if exists) or from Firebase database
     * @param userId - id of user to obtain
     * @param listener - receiver to return object asynchronously
     */
    void getUser(@NonNull String userId,
                 @NonNull SuccessCallback<User> listener,
                 @Nullable FailureCallback failureCallback);

//    /**
//     * Create new user in chat node with given parameters
//     * @param userId - user id
//     * @param name - display name
//     * @param image - image url
//     * @param callback - is invoked when user is created successfully
//     * @param failureCallback - is invoked when error occurs while creating user
//     */
//    void createUser(@NonNull String userId,
//                    @NonNull String name,
//                    @NonNull String image,
//                    @Nullable CompleteCallback callback,
//                    @Nullable FailureCallback failureCallback);

    User getCachedUser(String userId);

}
