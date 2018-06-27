package com.yeket.music.bridge.models.chat;

import com.stfalcon.chatkit.commons.models.IUser;

public class User implements IUser {

    private String id;
    private String name;
    private String avatar;

    public User(){}

    public User(String id, String name, String avatar){
        setId(id);
        setName(name);
        setAvatar(avatar);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
