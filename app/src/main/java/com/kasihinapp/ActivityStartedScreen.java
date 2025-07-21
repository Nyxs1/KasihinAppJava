package com.kasihinapp; // Pastikan package ini sesuai dengan proyek Anda

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityStartedScreen extends AppCompatActivity {

    // Deklarasikan semua komponen yang interaktif
    private Button googleSignInButton, EmailSignInButton; // Tambahkan tombol Email
    private TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.started_screen); // Pastikan layout ini memiliki semua tombol

        // Menghubungkan variabel dengan komponen di layout
        googleSignInButton = findViewById(R.id.googleSignInButton);
        EmailSignInButton = findViewById(R.id.EmailSignInButton); // Hubungkan tombol Email


        // Aksi untuk tombol "Login dengan Email"
        EmailSignInButton.setOnClickListener(v -> {
            // Pindah ke halaman EmailLoginActivity
            Intent intent = new Intent(ActivityStartedScreen.this, EmailLoginActivity.class);
            startActivity(intent);
        });

        // Aksi untuk tombol "Login dengan Google"
        googleSignInButton.setOnClickListener(v -> {
            // TODO: Ganti Toast ini dengan logika login Google yang sebenarnya
            Toast.makeText(ActivityStartedScreen.this, "Login Google diklik!", Toast.LENGTH_SHORT).show();
        });
    }
}