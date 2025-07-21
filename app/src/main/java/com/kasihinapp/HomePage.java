// HomePage.java

package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kasihinapp.search.SearchActivity; // <-- Pastikan import ini ada

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // 1. Temukan BottomNavigationView dari layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. Tandai item "Beranda" sebagai item yang aktif saat pertama kali dibuka
        bottomNavigationView.setSelectedItemId(R.id.navigation_beranda);

        // 3. Tambahkan listener untuk menangani klik pada setiap item menu
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_beranda) {
                // Anda sudah di halaman Beranda, tidak perlu aksi
                return true;
            } else if (itemId == R.id.navigation_eksplore) {
                // Pindah ke SearchActivity saat item "Eksplore" diklik
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                // overridePendingTransition(0, 0); // Opsional: hapus animasi transisi
                return true;
            } else if (itemId == R.id.navigation_scan) {
                // TODO: Tambahkan logika untuk pindah ke halaman Scan
                // Contoh: startActivity(new Intent(getApplicationContext(), ScanActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profil) {
                // TODO: Tambahkan logika untuk pindah ke halaman Profil
                // Contoh: startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}