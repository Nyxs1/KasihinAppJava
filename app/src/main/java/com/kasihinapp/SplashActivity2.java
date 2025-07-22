package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.kasihinapp.utils.SessionManager;

public class SplashActivity2 extends AppCompatActivity {

    private SessionManager sessionManager;
    private static final int SPLASH_DELAY = 2000; // Jeda 2 detik lagi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        sessionManager = new SessionManager(this);

        // Handler untuk mengecek sesi dan pindah ke halaman yang sesuai
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionManager.getAuthToken() != null) {
                // Jika sudah login, ke HomePage
                startActivity(new Intent(SplashActivity2.this, HomePage.class));
            } else {
                // Jika belum, ke halaman welcome
                startActivity(new Intent(SplashActivity2.this, ActivityStartedScreen.class));
            }
            // Tutup activity ini agar tidak bisa kembali
            finish();
        }, SPLASH_DELAY);
    }
}