package com.kasihinapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private String role;

    @SerializedName("poin")
    private int poin;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("bio_user")
    private String bioUser;

    // Constructor kosong, berguna untuk beberapa library
    public User() {}

    // Constructor lengkap yang sudah diperbarui
    public User(int id, String nama, String email, String password, String role, int poin, String createdAt, String bioUser) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.role = role;
        this.poin = poin;
        this.createdAt = createdAt;
        this.bioUser = bioUser; // Ditambahkan
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getPoin() { return poin; }
    public void setPoin(int poin) { this.poin = poin; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Getter dan Setter untuk bioUser
    public String getBioUser() { return bioUser; }
    public void setBioUser(String bioUser) { this.bioUser = bioUser; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", poin=" + poin +
                ", createdAt='" + createdAt + '\'' +
                ", bioUser='" + bioUser + '\'' + // Ditambahkan
                '}';
    }

    // di dalam class User
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id; // Membandingkan berdasarkan ID
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id).hashCode(); // Menggunakan ID untuk hashCode
    }
}