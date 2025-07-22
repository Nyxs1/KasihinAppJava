// di package com.kasihinapp.model
package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;

public class DonationResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    // Getter methods
    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}