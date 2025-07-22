package com.kasihinapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kasihinapp.R;
import com.kasihinapp.model.ProfileResponse;
import com.kasihinapp.model.User;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.UserApiService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView ivProfile;
    private TextView tvDisplayName, tvFullName, tvBio;
    private Button btnDonasi;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ScrollView contentScrollView;
    private UserApiService apiService;
    private User currentUser; // Untuk menyimpan data user yang didapat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi Views
        initViews();

        // Setup Toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inisialisasi API Service
        apiService = ApiClient.getClient().create(UserApiService.class);

        // Ambil ID dari Intent dan muat data
        int userId = getIntent().getIntExtra("user_id", -1);
        if (userId != -1) {
            fetchUserProfile(userId);
        } else {
            Toast.makeText(this, "ID Pengguna tidak valid.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Setup tombol donasi
        btnDonasi.setOnClickListener(v -> {
            if (currentUser != null) {
                Intent i = new Intent(ProfileActivity.this, DonasiActivity.class);
                i.putExtra("user_id", currentUser.getId());
                i.putExtra("user_name", currentUser.getNama());
                // i.putExtra("user_image", ...); // Tambahkan jika ada URL gambar
                startActivity(i);
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProfile = findViewById(R.id.ivProfile);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvFullName = findViewById(R.id.tvFullName);
        tvBio = findViewById(R.id.tvBio);
        btnDonasi = findViewById(R.id.btnDonasi);
        progressBar = findViewById(R.id.progressBar);
        contentScrollView = findViewById(R.id.contentScrollView);
    }

    private void fetchUserProfile(int userId) {
        showLoading(true);
        apiService.getUserProfile(userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    currentUser = response.body().getUser();
                    populateUI(currentUser);
                } else {
                    Toast.makeText(ProfileActivity.this, "Gagal memuat profil.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(User user) {
        toolbar.setTitle("Profile - " + user.getNama());
        tvDisplayName.setText(user.getNama().toUpperCase());
        tvFullName.setText(user.getNama()); // Ganti dengan nama lengkap jika ada di database
        tvBio.setText(user.getBioUser()); // Ganti dengan data bio jika ada
        // Logika untuk gambar profil akan ditambahkan jika ada URL gambar dari API
        // Glide.with(this).load(user.getGambarUrl()).into(ivProfile);

        showLoading(false);
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.VISIBLE);
        contentScrollView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}