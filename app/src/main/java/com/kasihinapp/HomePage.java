package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kasihinapp.model.DonationHistory;
import com.kasihinapp.model.DonationHistoryResponse;
import com.kasihinapp.model.ProfileResponse;
import com.kasihinapp.model.User;
import com.kasihinapp.search.SearchActivity;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.SessionManager;
import com.kasihinapp.utils.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {

    private SessionManager sessionManager;
    private UserApiService apiService;

    // Deklarasi View
    private TextView tvUserName, tvUserRole, tvPoinBalance;
    private ImageView ivToggleBalance; // <-- View baru
    private RecyclerView rvRecentDonations;

    // Variabel untuk data
    private DonationHistoryAdapter donationAdapter;
    private List<DonationHistory> donationList = new ArrayList<>();
    private User currentUser; // <-- Menyimpan data user
    private boolean isBalanceVisible = true; // <-- Flag untuk status poin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(UserApiService.class);

        initViews();
        setupBottomNav();
        setupListeners(); // <-- Panggil method listener baru

        int userId = getUserIdFromToken();
        if (userId != -1) {
            fetchUserProfile(userId);
            fetchDonationHistory();
        } else {
            // Jika token tidak valid, paksa logout
            sessionManager.clearSession();
            startActivity(new Intent(this, ActivityStartedScreen.class));
            finish();
        }
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvPoinBalance = findViewById(R.id.tvPoinBalance);
        ivToggleBalance = findViewById(R.id.ivToggleBalance); // <-- Inisialisasi ImageView mata

        rvRecentDonations = findViewById(R.id.rvRecentDonations);
        rvRecentDonations.setLayoutManager(new LinearLayoutManager(this));
        donationAdapter = new DonationHistoryAdapter(donationList);
        rvRecentDonations.setAdapter(donationAdapter);
    }

    private void fetchUserProfile(int userId) {
        apiService.getUserProfile(userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    currentUser = response.body().getUser();
                    tvUserName.setText(currentUser.getNama());
                    tvUserRole.setText(currentUser.getRole());
                    updateBalanceView(); // Panggil method baru untuk update tampilan poin
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(HomePage.this, "Gagal memuat profil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ... (fetchDonationHistory, getUserIdFromToken, setupBottomNav tetap sama) ...

    private void setupListeners() {
        ivToggleBalance.setOnClickListener(v -> {
            // Balikkan status visibilitas
            isBalanceVisible = !isBalanceVisible;
            // Perbarui tampilan
            updateBalanceView();
        });
    }

    /**
     * Method baru untuk memperbarui tampilan saldo poin
     */
    private void updateBalanceView() {
        if (currentUser == null) return;

        if (isBalanceVisible) {
            // Jika terlihat, tampilkan jumlah poin asli
            tvPoinBalance.setText(currentUser.getPoin() + " POINT");
            ivToggleBalance.setImageResource(R.drawable.ic_visibility);
        } else {
            // Jika disembunyikan, tampilkan bintang
            tvPoinBalance.setText("**** POINT");
                ivToggleBalance.setImageResource(R.drawable.ic_visibility_off);
        }
    }

    private void fetchDonationHistory() {
        apiService.getDonationHistory().enqueue(new Callback<DonationHistoryResponse>() {
            @Override
            public void onResponse(Call<DonationHistoryResponse> call, Response<DonationHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    donationList.clear();
                    donationList.addAll(response.body().getHistory());
                    donationAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<DonationHistoryResponse> call, Throwable t) {
                Toast.makeText(HomePage.this, "Gagal memuat riwayat donasi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getUserIdFromToken() {
        String token = sessionManager.getAuthToken();
        if (token != null) {
            try {
                JWT jwt = new JWT(token);
                String userIdStr = jwt.getSubject();
                if (userIdStr != null) {
                    return Integer.parseInt(userIdStr);
                }
            } catch (Exception e) { return -1; }
        }
        return -1;
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_beranda);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_beranda) return true;
            if (itemId == R.id.navigation_eksplore) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                return true;
            }
            // ... (logika item lain)
            return false;
        });
    }
}