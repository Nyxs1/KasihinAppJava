package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Import View
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT; // Import JWT
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kasihinapp.model.LogoutResponse;
import com.kasihinapp.model.ProfileResponse;
import com.kasihinapp.model.User;
import com.kasihinapp.search.SearchActivity;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.SessionManager;
import com.kasihinapp.utils.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUserActivity extends AppCompatActivity {

    private static final String TAG = "ProfileUserActivity";

    private Button btnLogout;
    private SessionManager sessionManager;
    private UserApiService apiService;

    private TextView tvUserName;
    private TextView tvUserRole;
    private ImageView profileImage;

    // Tidak perlu lagi userIdToDisplay dari Intent, karena akan diambil dari token
    // private int userIdToDisplay = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        // Inisialisasi
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(UserApiService.class);

        // Inisialisasi Views dari layout
        btnLogout = findViewById(R.id.btnLogout);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        profileImage = findViewById(R.id.profile_image);

        // --- Logika untuk mendapatkan user ID dari token JWT ---
        int userId = getUserIdFromToken();
        if (userId != -1) {
            // Jika user ID berhasil didapatkan dari token, fetch profilnya
            Log.d(TAG, "Melihat profil pengguna yang sedang login (ID dari token): " + userId);
            fetchUserProfile(userId);
            btnLogout.setVisibility(View.VISIBLE); // Pastikan tombol logout terlihat
        } else {
            // Jika token tidak valid atau tidak ada user ID di dalamnya
            Toast.makeText(this, "Sesi Anda tidak valid. Silakan login kembali.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "User ID tidak ditemukan dari token JWT.");
            // Paksa logout dan arahkan ke halaman awal
            sessionManager.clearSession(); //
            Intent intent = new Intent(ProfileUserActivity.this, ActivityStartedScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return; // Penting: Hentikan eksekusi selanjutnya
        }

        // Setup listener untuk tombol logout
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });

        // Setup BottomNavigationView (kode yang sudah ada)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profil); //
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profil) {
                return true;
            } else if (itemId == R.id.navigation_beranda) { //
                startActivity(new Intent(getApplicationContext(), HomePage.class)); //
                overridePendingTransition(0,0);
                return true;
            } else if (itemId == R.id.navigation_eksplore) { //
                startActivity(new Intent(getApplicationContext(), SearchActivity.class)); //
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });
    }

    private void fetchUserProfile(int id) {
        Log.d(TAG, "Memanggil API untuk profil user ID: " + id);
        apiService.getUserProfile(id).enqueue(new Callback<ProfileResponse>() { //
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) { //
                    User user = response.body().getUser(); //
                    if (user != null) {
                        tvUserName.setText(user.getNama()); //
                        tvUserRole.setText(user.getRole()); //
                        // Gambar profil (jika ada URL di User model)
                        // Glide.with(ProfileUserActivity.this).load(user.getImageUrl()).into(profileImage);
                        Log.d(TAG, "Profil user berhasil dimuat: " + user.getNama() + ", Role: " + user.getRole());
                    } else {
                        Toast.makeText(ProfileUserActivity.this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Objek user null di ProfileResponse.");
                    }
                } else {
                    String errorMessage = "Gagal memuat profil. Kode: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Error: " + response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                    }
                    Toast.makeText(ProfileUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Gagal fetch profil user: " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileUserActivity.this, "Gagal koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Panggilan API gagal untuk profil user", t);
            }
        });
    }

    private void performLogout() {
        String token = sessionManager.getAuthToken(); //
        if (token == null) {
            redirectToLogin();
            return;
        }

        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        Call<LogoutResponse> call = apiService.logout("Bearer " + token); //
        call.enqueue(new Callback<LogoutResponse>() { //
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) { //
                    Toast.makeText(ProfileUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show(); //
                    redirectToLogin();
                } else {
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
        sessionManager.clearSession(); //
        Intent intent = new Intent(ProfileUserActivity.this, ActivityStartedScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Mengekstrak user ID dari token JWT yang disimpan di SessionManager.
     * Mirip dengan implementasi di HomePage.java.
     * @return User ID jika berhasil diekstrak, -1 jika token tidak valid atau ID tidak ditemukan.
     */
    private int getUserIdFromToken() {
        String token = sessionManager.getAuthToken(); //
        if (token != null) {
            try {
                JWT jwt = new JWT(token); //
                // Asumsi subject dari JWT adalah user ID
                String userIdStr = jwt.getSubject(); //
                if (userIdStr != null) {
                    return Integer.parseInt(userIdStr); //
                }
            } catch (Exception e) {
                Log.e(TAG, "Gagal mengurai token JWT: " + e.getMessage());
                return -1;
            }
        }
        return -1;
    }
}