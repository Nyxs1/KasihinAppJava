package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("user")
    private User user;

    // Getter methods
    public boolean isStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}