package com.yeket.music.bridge.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yeket.music.bridge.datasource.users.UsersDataSource;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.models.chat.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
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
// TODO: 03.01.18 add error reporting for null pointers
//if needed
// null pointer due to new instances. obviously need singleton
@Singleton
public class ChatUserManagerImpl implements ChatUserManager {

    private static final String TAG = ChatUserManagerImpl.class.getSimpleName();

    private Map<String, User> cachedUsers;

    private UsersDataSource usersDataSource;

//    private static ChatUserManagerImpl INSTANCE;
//
//    public static ChatUserManagerImpl instance(UsersDataSource usersDataSource){
//        if(INSTANCE == null){
//            synchronized (ChatUserManagerImpl.class){
//                if(INSTANCE == null)
//                    INSTANCE = new ChatUserManagerImpl(usersDataSource);
//            }
//        }
//
//        return INSTANCE;
//    }

    /**
     * Constructor
     * Create connections to:
     * - base chat node
     * - all messages node
     * - all dialogs node
     * - all chat users node
     * Create listener object for new messages in all dialogs
     * Create listener for new dialogs for given user
     */
    @Inject
    public ChatUserManagerImpl(UsersDataSource usersDataSource){

        this.usersDataSource = usersDataSource;

        cachedUsers = new HashMap<>();
        // TODO: 02.01.18 admin user data
        // TODO: 03.01.18 default user data
        cachedUsers.put(Default.Chat.ADMIN_ID, Default.Chat.ADMIN);
        cachedUsers.put(Default.Chat.USER_ID, Default.Chat.USER);

    }

    /**
     * return User object by userId asynchronously
     * from local cache (if exists) or from Firebase database
     * @param userId - id of user to obtain
     * @param callback - callback for async success response with user
     * @param failureCallback - callback for async failure response while getting user
     */
    @Override
    public void getUser(@NonNull String userId,
                        @NonNull SuccessCallback<User> callback,
                        @Nullable FailureCallback failureCallback) {
        User cachedUser = getCachedUser(userId);

        if (cachedUser == null) {
            usersDataSource.getUser(userId,
                    luresUser -> {
                        User user = new User(
                                luresUser.getId(),
                                luresUser.getDisplayName(),
                                luresUser.getImageUri());
                        cachedUsers.put(userId, user);
                        callback.onSuccess(user);
                    }, failureCallback);
        } else {
            callback.onSuccess(cachedUser);
        }
    }

    @Override
    public User getCachedUser(String userId){
        return cachedUsers.get(userId);
    }

}
