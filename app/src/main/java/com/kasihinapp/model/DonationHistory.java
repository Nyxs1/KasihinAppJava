package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;

public class DonationHistory {
    @SerializedName("dari_nama")
    private String dariNama;

    @SerializedName("ke_nama")
    private String keNama;

    @SerializedName("jumlah")
    private int jumlah;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("tanggal")
    private String tanggal;

    // Getters
    public String getDariNama() { return dariNama; }
    public String getKeNama() { return keNama; }
    public int getJumlah() { return jumlah; }
    public String getPesan() { return pesan; }
    public String getTanggal() { return tanggal; }
}