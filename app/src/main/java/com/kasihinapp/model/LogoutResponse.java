// di package com.kasihinapp.model
package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    // Getters
    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
}