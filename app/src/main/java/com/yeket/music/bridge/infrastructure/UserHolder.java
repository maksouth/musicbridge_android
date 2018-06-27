package com.yeket.music.bridge.infrastructure;

import android.util.Log;

import com.yeket.music.bridge.models.LuresUser;
import com.yeket.music.bridge.models.chat.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserHolder {

    private LuresUser user = new LuresUser();
    private User chatUser = new User();

    @Inject
    public UserHolder(){
        Log.d(UserHolder.class.getSimpleName(), "Create new " + this);
    }

    // will be replaced with dagger

    public void store(LuresUser user){
        this.user.copyFrom(user);

        chatUser.setId(user.getId());
        chatUser.setName(user.getDisplayName());
        chatUser.setAvatar(user.getImageUri());

    }

    public void store(User chatUser){

        this.chatUser = chatUser;
        LuresUser luresUser = new LuresUser();
        luresUser.setId(chatUser.getId());
        luresUser.setDisplayName(chatUser.getName());
        luresUser.setImageUri(chatUser.getAvatar());

        this.user.copyFrom(luresUser);
    }

    public LuresUser getUser(){
        return user;
    }

    public User getChatUser(){
        return chatUser;
    }

}
