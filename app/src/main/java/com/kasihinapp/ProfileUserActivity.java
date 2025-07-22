package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // <-- Import Button
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kasihinapp.model.LogoutResponse; // <-- Import model
import com.kasihinapp.search.SearchActivity;
import com.kasihinapp.utils.ApiClient; // <-- Import
import com.kasihinapp.utils.SessionManager; // <-- Import
import com.kasihinapp.utils.UserApiService; // <-- Import

import retrofit2.Call; // <-- Import
import retrofit2.Callback; // <-- Import
import retrofit2.Response; // <-- Import

public class ProfileUserActivity extends AppCompatActivity {

    private Button btnLogout;
    private SessionManager sessionManager;
    private UserApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        // Inisialisasi
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(UserApiService.class);

        // Inisialisasi Tombol Logout
        btnLogout = findViewById(R.id.btnLogout);

        // Setup listener untuk tombol logout
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });

        // ... (kode setup BottomNavigationView tetap sama)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profil);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profil) {
                return true;
            } else if (itemId == R.id.navigation_beranda) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                return true;
            } else if (itemId == R.id.navigation_eksplore) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                return true;
            }
            return false;
        });
    }

    private void performLogout() {
        String token = sessionManager.getAuthToken();
        if (token == null) {
            redirectToLogin();
            return;
        }

        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        Call<LogoutResponse> call = apiService.logout("Bearer " + token);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(ProfileUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                } else {
                    // Jika token sudah tidak valid, tetap paksa logout dari sisi client
                    Toast.makeText(ProfileUserActivity.this, "Sesi Anda telah berakhir.", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(ProfileUserActivity.this, "Logout gagal. Periksa koneksi internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectToLogin() {
        // Hapus data sesi dari SharedPreferences
        sessionManager.clearSession();

        // Arahkan ke halaman awal
        Intent intent = new Intent(ProfileUserActivity.this, ActivityStartedScreen.class);
        // Hapus semua activity sebelumnya dari back stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}