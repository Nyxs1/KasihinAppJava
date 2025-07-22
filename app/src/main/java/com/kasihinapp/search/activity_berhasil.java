package com.kasihinapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.kasihinapp.HomePage;
import com.kasihinapp.R;

public class activity_berhasil extends AppCompatActivity {

    // Durasi tampilan dalam milidetik (5 detik)
    private static final int SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berhasil);

        // Menggunakan Handler untuk menunda perpindahan ke HomePage
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Buat Intent untuk pindah ke HomePage
                Intent intent = new Intent(activity_berhasil.this, HomePage.class);

                // Flag ini penting untuk menghapus semua activity sebelumnya dari tumpukan (stack),
                // sehingga pengguna tidak bisa menekan tombol "kembali" ke halaman donasi atau berhasil.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Tutup activity ini agar tidak ada di tumpukan lagi
                finish();
            }
        }, SPLASH_DELAY);
    }
}