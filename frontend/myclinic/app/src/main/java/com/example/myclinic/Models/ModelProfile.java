package com.example.myclinic.Models;

public class ModelProfile {
    String userId, userImg;

    public ModelProfile() {
    }

    public ModelProfile(String userId, String userImg) {
        this.userId = userId;
        this.userImg = userImg;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImg() {
        return userImg;
    }
}
