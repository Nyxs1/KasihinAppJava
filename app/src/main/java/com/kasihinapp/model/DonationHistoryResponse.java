package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DonationHistoryResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("history")
    private List<DonationHistory> history;

    // Getters
    public boolean isStatus() { return status; }
    public List<DonationHistory> getHistory() { return history; }
}