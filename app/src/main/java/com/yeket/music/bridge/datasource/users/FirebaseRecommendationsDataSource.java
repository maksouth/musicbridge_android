package com.yeket.music.bridge.datasource.users;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.yeket.music.bridge.constants.errors.Errors;
import com.yeket.music.bridge.datasource.likes.LikesDataSource;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.models.AgeRange;
import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.infrastructure.utils.DateUtils;
import com.yeket.music.bridge.infrastructure.callbacks.CompleteCallback;
import com.yeket.music.bridge.infrastructure.callbacks.FailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.LoggerFailureCallback;
import com.yeket.music.bridge.infrastructure.callbacks.SuccessCallback;
import com.yeket.music.bridge.services.UserManager;

import java.util.Date;

import javax.inject.Inject;

public class FirebaseRecommendationsDataSource extends FirebaseUsersDataSource implements RecommendationsDataSource {

    private static final String TAG = RecommendationsDataSource.class.getSimpleName();

    private LikesDataSource likesDataSource;
    private UserPreferencesSource preferencesSource;

    private Integer userProcessed = 0;

    @Inject
    public FirebaseRecommendationsDataSource(LikesDataSource likesDataSource, UserPreferencesSource preferencesSource){
        this.likesDataSource = likesDataSource;
        this.preferencesSource = preferencesSource;
    }

    @Override
    public void getAllRecommendations(String userId,
                                      SuccessCallback<LuresUser> nextUserReceivedCallback,
                                      FailureCallback failureCallback,
                                      CompleteCallback completeCallback) {

        userProcessed = 0;

        getUser(userId, (LuresUser currentUser) ->
                        mDatabaseUsers.orderByChild(Schemes.User.GENDER)
                                .equalTo(getOppositeGender(currentUser.getGender()))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Iterable<DataSnapshot> userDataSnapshotList = dataSnapshot.getChildren();
                                        long totalUsers = dataSnapshot.getChildrenCount();

                                        if (totalUsers == 0) failureCallback.onFailure(Errors.GET_RECOMMENDATIONS_NOT_FOUND);

                                        preferencesSource.getAgeRange(currentUser.getId(),
                                                ageRange -> {

                                                    for (DataSnapshot userSnapshot : userDataSnapshotList) {
                                                        try {
                                                            LuresUser tempUser = readUserFromDataSnapshot(userSnapshot);
                                                            isRecommended(ageRange, currentUser, tempUser, isRecommended -> {
                                                                Log.d(TAG, "Is " + tempUser.getDisplayName() + " recommended: " + isRecommended);
                                                                if (isRecommended)
                                                                    nextUserReceivedCallback.onSuccess(tempUser);
                                                                userProcessed++;
                                                                if (userProcessed == totalUsers) {
                                                                    Log.d(TAG, "Finished on user " + tempUser.getDisplayName());
                                                                    completeCallback.onComplete();
                                                                }
                                                            });
                                                        } catch (IllegalArgumentException ex) {
                                                            userProcessed++;
                                                            if (userProcessed == totalUsers) {
                                                                Log.d(TAG, "Finished on " + userProcessed);
                                                                completeCallback.onComplete();
                                                            }
                                                        }
                                                    }
                                                },
                                                failureCallback);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        failureCallback.onFailure(Errors.GET_RECOMMENDATIONS_NOT_FOUND);
                                    }
                                }),
                (LoggerFailureCallback)err -> failureCallback.onFailure(Errors.GET_RECOMMENDATIONS_NOT_FOUND));
    }

    private String getOppositeGender(String gender){
        if(UserManager.GENDER_MALE.equals(gender)){
            return UserManager.GENDER_FEMALE;
        }

        return UserManager.GENDER_MALE;
    }

    /**
     * check if user is recommended for currentUser
     * Criteria:
     *  user age
     *
     * @param currentUser
     * @param user
     * @param callback - callback if user is recommended for current user
     */
    private void isRecommended (AgeRange ageRange, LuresUser currentUser, LuresUser user, SuccessCallback<Boolean> callback) {

        if (!currentUser.getGender().equals(user.getGender())) {

            int userAge = DateUtils.getDiffYears(new Date(user.getBirthdate()), new Date());
            //check if recommended user has desired age
            if (userAge < ageRange.getMinAge() || userAge > ageRange.getMaxAge()) {
                Log.d(TAG, user.getDisplayName() + " age " + userAge + " is not in range " + ageRange.getMinAge() + "-" + ageRange.getMaxAge());
                callback.onSuccess(false);
                return;
            }

            likesDataSource.isUserLiked(user.getId(), currentUser.getId(), isLiked -> {
                // if recommended user is already liked
                // then do not show again
                if (isLiked) {
                    Log.d(TAG, "User " + user.getDisplayName() + " is liked already");
                    callback.onSuccess(false);
                    return;
                }

                likesDataSource.isUserDisliked(user.getId(), currentUser.getId(), isDisliked -> {
                    //last check
                    // if recommended user was already disliked
                    Log.d(TAG, "User " + user.getDisplayName() + " is disliked: " + isDisliked);
                    callback.onSuccess(!isDisliked);
                });
            });
        }
    }

}
