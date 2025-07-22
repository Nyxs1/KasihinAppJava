package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.kasihinapp.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Anda bisa membuat layout sederhana untuk splash screen jika mau
        // setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        // Handler untuk memberi jeda singkat (misal: 1.5 detik)
        new Handler().postDelayed(() -> {
            // Cek apakah ada token yang tersimpan
            if (sessionManager.getAuthToken() != null) {
                // Jika ada, langsung ke HomePage
                startActivity(new Intent(SplashActivity.this, HomePage.class));
            } else {
                // Jika tidak ada, ke halaman login/welcome
                startActivity(new Intent(SplashActivity.this, ActivityStartedScreen.class));
            }
            // Tutup SplashActivity agar tidak bisa kembali
            finish();
        }, 1500);
    }
}