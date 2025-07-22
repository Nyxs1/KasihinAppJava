package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // Jeda 2 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler untuk pindah ke splash screen kedua setelah jeda
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Pindah ke SplashActivity2
            startActivity(new Intent(SplashActivity.this, SplashActivity2.class));
            // Tutup activity ini agar tidak bisa kembali
            finish();
        }, SPLASH_DELAY);
    }
}