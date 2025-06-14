package com.alizainsolutions.textme.Model;

public class MessageModel {
    String senderId, receiverId, message, time;
    Boolean isSender;
    public MessageModel(String senderId, String receiverId, String message, String time, Boolean isSender) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.time = time;
        this.isSender = isSender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getSender() {
        return isSender;
    }

    public void setSender(Boolean sender) {
        isSender = sender;
    }
}
