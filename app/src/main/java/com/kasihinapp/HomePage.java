package com.kasihinapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout; // <-- Import
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
    private SwipeRefreshLayout swipeRefreshLayout; // <-- View baru
    private TextView tvUserName, tvUserRole, tvPoinBalance;
    private RecyclerView rvRecentDonations;
    private DonationHistoryAdapter donationAdapter;
    private List<DonationHistory> donationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient().create(UserApiService.class);

        initViews();
        setupBottomNav();

        // Setup listener untuk SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Panggil method untuk refresh data saat ditarik
            refreshData();
        });

        // Panggil data untuk pertama kali saat halaman dibuka
        refreshData();
    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvPoinBalance = findViewById(R.id.tvPoinBalance);

        rvRecentDonations = findViewById(R.id.rvRecentDonations);
        rvRecentDonations.setLayoutManager(new LinearLayoutManager(this));
        donationAdapter = new DonationHistoryAdapter(donationList);
        rvRecentDonations.setAdapter(donationAdapter);
    }

    /**
     * Method baru untuk membungkus semua proses pengambilan data.
     */
    private void refreshData() {
        int userId = getUserIdFromToken();
        if (userId != -1) {
            fetchUserProfile(userId);
            fetchDonationHistory(userId);
        } else {
            // Jika token tidak valid, hentikan refresh dan paksa logout
            swipeRefreshLayout.setRefreshing(false);
            sessionManager.clearSession();
            startActivity(new Intent(this, ActivityStartedScreen.class));
            finish();
        }
    }

    private void fetchUserProfile(int userId) {
        apiService.getUserProfile(userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    User user = response.body().getUser();
                    tvUserName.setText(user.getNama());
                    tvUserRole.setText(user.getRole());
                    tvPoinBalance.setText("POIN " + user.getPoin());
                }
            }
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(HomePage.this, "Gagal memuat profil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDonationHistory(int userId) {
        // Tampilkan ikon loading
        swipeRefreshLayout.setRefreshing(true);

        apiService.getDonationHistory(userId).enqueue(new Callback<DonationHistoryResponse>() {
            @Override
            public void onResponse(Call<DonationHistoryResponse> call, Response<DonationHistoryResponse> response) {
                // Sembunyikan ikon loading setelah selesai
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    donationList.clear();
                    donationList.addAll(response.body().getHistory());
                    donationAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<DonationHistoryResponse> call, Throwable t) {
                // Sembunyikan ikon loading jika gagal
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(HomePage.this, "Gagal memuat riwayat donasi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ... (method getUserIdFromToken dan setupBottomNav tetap sama) ...
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
            if (itemId == R.id.navigation_profil) {
                startActivity(new Intent(getApplicationContext(), ProfileUserActivity.class));
                return true;
            }
            return false;
        });
    }
}