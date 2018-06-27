package com.yeket.music.bridge.models.chat;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;

public class Dialog implements IDialog {

    private String id;
    private String photo;
    private String name;
    private List<IUser> users;
    private IMessage lastMessage;
    private int unreadCounts;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return photo;
    }

    @Override
    public String getDialogName() {
        return name;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = message;
    }

    @Override
    public int getUnreadCount() {
        return unreadCounts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<IUser> users) {
        this.users = users;
    }

    public void setUnreadCounts(int unreadCounts) {
        this.unreadCounts = unreadCounts;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "id='" + id + '\'' +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
