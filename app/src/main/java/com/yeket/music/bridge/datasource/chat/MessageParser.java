package com.yeket.music.bridge.datasource.chat;

import com.google.firebase.database.DataSnapshot;
import com.yeket.music.bridge.datasource.util.Checker;
import com.yeket.music.bridge.datasource.util.Default;
import com.yeket.music.bridge.datasource.util.Schemes;
import com.yeket.music.bridge.models.chat.Message;
import com.yeket.music.bridge.models.chat.User;
import com.yeket.music.bridge.services.ChatUserManager;

import java.util.Date;

import javax.inject.Inject;

import static com.yeket.music.bridge.datasource.util.Checker.extract;

public class MessageParser {

    private ChatUserManager dataSource;

    @Inject
    public MessageParser(ChatUserManager dataSource){
        this.dataSource = dataSource;
    }

    public Message extract(DataSnapshot snapshot){
        Message message = null;

        if(snapshot.exists()) {

            message = new Message();

            String userId = Checker.extract(snapshot.child(Schemes.Chat.SENDER_ID).getValue(), Default.Chat.USER_ID);
            User sender = dataSource.getCachedUser(userId);

            long timestamp = Checker.extract(snapshot.child(Schemes.Chat.TIMESTAMP).getValue(), new Date().getTime());
            String payload = Checker.extract(snapshot.child(Schemes.Chat.PAYLOAD).getValue(), Default.Chat.MESSAGE_PAYLOAD);
            String messageId = Checker.extract(snapshot.child(Schemes.Chat.MESSAGE_ID).getValue(), Default.Chat.MESSAGE_ID);
            boolean isRead = Checker.extract(snapshot.child(Schemes.Chat.IS_READ).getValue(), Default.Chat.IS_READ);

            message.setUser(sender);
            message.setId(messageId);
            message.setText(payload);
            message.setRead(isRead);
            message.setCreatedAt(new Date(timestamp));

        }

        return message;
    }

}
