package com.alizainsolutions.textme.Model;

public class PeopleModel {
    String userName, userBio, userId;

    public PeopleModel(String userName, String userBio, String userId) {
        this.userName = userName;
        this.userBio = userBio;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
