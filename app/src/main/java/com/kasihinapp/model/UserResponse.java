// com.kasihinapp.model.UserResponse.java
package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("users")
    private List<User> users;

    public boolean isStatus() { return status; }
    public List<User> getUsers() { return users; }
}