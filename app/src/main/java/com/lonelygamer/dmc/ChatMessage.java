package com.lonelygamer.dmc;

import java.util.Date;

/**
 * Created by dyl on 7/27/17.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String messageDisplayName;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser, String messageDisplayName) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageDisplayName = messageDisplayName;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageDisplayName() {
        return messageDisplayName;
    }

    public void setMessageDisplayName(String messageDisplayName) {
        this.messageDisplayName = messageDisplayName;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
