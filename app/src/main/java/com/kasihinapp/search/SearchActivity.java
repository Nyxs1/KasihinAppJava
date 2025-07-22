package com.kasihinapp.search;

import android.content.Context;
import android.content.Intent; // Import Intent
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kasihinapp.R;
import com.kasihinapp.model.User;
import com.kasihinapp.model.UserResponse;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.UserApiService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private static final String PREFS_NAME = "SearchActivityPrefs";
    private static final String KEY_RECENT_SEARCHES = "recent_searches";
    private static final int MAX_RECENT_SEARCHES = 5; // Batasi jumlah pencarian terakhir

    private UserAdapter adapter;
    private List<User> allUsersFromApi = new ArrayList<>(); // Daftar semua user dari API
    private List<User> displayedUserList = new ArrayList<>(); // Daftar yang ditampilkan
    private List<User> recentSearches = new ArrayList<>(); // Menggunakan ArrayList untuk pencarian terakhir

    private EditText etSearch;
    private UserApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvSectionTitle;
//    private ImageView backArrow;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        apiService = ApiClient.getClient().create(UserApiService.class);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        setupRecyclerView();
        setupListeners();
        loadRecentSearches(); // Muat pencarian terakhir saat aktivitas dimulai
        displayRecentSearches(); // Tampilkan pencarian terakhir secara default
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);
//        backArrow = findViewById(R.id.back_arrow);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(displayedUserList);
        recyclerView.setAdapter(adapter);

        // Listener klik item pada adapter untuk menyimpan ke recent searches dan navigasi
        adapter.setOnItemClickListener(user -> {
            addRecentSearch(user); // Simpan user yang diklik sebagai pencarian terakhir
            // Navigasi ke ProfileActivity
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            intent.putExtra("user_id", user.getId());
            intent.putExtra("user_name", user.getNama());
            startActivity(intent);
        });
    }

    private void setupListeners() {
//        backArrow.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    displayRecentSearches(); // Tampilkan pencarian terakhir jika input kosong
                } else {
                    tvSectionTitle.setText("Hasil Pencarian");
                    fetchAndFilterUsers(keyword); // Lakukan pencarian dari API jika ada keyword
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadRecentSearches() {
        String json = sharedPreferences.getString(KEY_RECENT_SEARCHES, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<User>>() {}.getType(); // Ubah ke ArrayList
            List<User> loadedList = gson.fromJson(json, type);
            if (loadedList != null) {
                recentSearches.clear();
                recentSearches.addAll(loadedList);
            }
        }
    }

    private void saveRecentSearches() {
        Gson gson = new Gson();
        String json = gson.toJson(recentSearches);
        sharedPreferences.edit().putString(KEY_RECENT_SEARCHES, json).apply();
    }

    private void addRecentSearch(User user) {
        // Hapus duplikat (jika user sudah ada, pindahkan ke paling atas)
        // Kita perlu mencari user berdasarkan ID atau properti unik lainnya
        int existingIndex = -1;
        for (int i = 0; i < recentSearches.size(); i++) {
            if (recentSearches.get(i).getId() == user.getId()) { // Asumsi ID adalah unik
                existingIndex = i;
                break;
            }
        }
        if (existingIndex != -1) {
            recentSearches.remove(existingIndex);
        }

        recentSearches.add(0, user); // Tambahkan ke awal daftar

        // Batasi jumlah pencarian terakhir
        while (recentSearches.size() > MAX_RECENT_SEARCHES) {
            recentSearches.remove(recentSearches.size() - 1); // Hapus elemen terakhir
        }
        saveRecentSearches();
    }

    private void displayRecentSearches() {
        tvSectionTitle.setText("Pencarian Terakhir");
        displayedUserList.clear();
        displayedUserList.addAll(recentSearches);
        adapter.notifyDataSetChanged();
        showData(!displayedUserList.isEmpty());
        // Jika tidak ada pencarian terakhir, tampilkan pesan kosong
        if (displayedUserList.isEmpty()) {
            tvEmpty.setText("Tidak ada pencarian terakhir.");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchAndFilterUsers(String keyword) {
        if (allUsersFromApi.isEmpty()) {
            // Jika data belum ada, ambil dari API terlebih dahulu
            fetchRecipients(keyword);
        } else {
            // Jika data sudah ada, langsung filter di sisi klien
            filterUsersLocally(keyword);
        }
    }

    private void fetchRecipients(final String currentKeyword) {
        showLoading(true);
        Log.d(TAG, "Memulai panggilan API ke server untuk penerima...");

        apiService.getRecipients().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<User> receivedUsers = response.body().getUsers();
                    if (receivedUsers != null && !receivedUsers.isEmpty()) {
                        allUsersFromApi.clear();
                        allUsersFromApi.addAll(receivedUsers);
                        filterUsersLocally(currentKeyword); // Filter dengan keyword setelah data diambil
                    } else {
                        Log.d(TAG, "Respons berhasil, tapi daftar pengguna kosong.");
                        showData(false);
                        tvEmpty.setText("Tidak ada pengguna ditemukan.");
                    }
                } else {
                    Log.e(TAG, "Respons dari server tidak berhasil atau status=false. Kode: " + response.code());
                    showData(false);
                    tvEmpty.setText("Gagal memuat data dari server.");
                    Toast.makeText(SearchActivity.this, "Gagal memuat data dari server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                showLoading(false);
                showData(false);
                Log.e(TAG, "Panggilan API gagal total (onFailure).", t);
                tvEmpty.setText("Error Jaringan: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterUsersLocally(String keyword) {
        displayedUserList.clear();
        if (!keyword.isEmpty()) {
            for (User user : allUsersFromApi) {
                if (user.getNama().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))) {
                    displayedUserList.add(user);
                }
            }
        } else {
            // Ini seharusnya tidak terpanggil jika logic di onTextChanged benar
            displayRecentSearches(); // Pastikan kembali ke recent searches jika keyword dikosongkan setelah filter
            return;
        }
        adapter.notifyDataSetChanged();
        showData(!displayedUserList.isEmpty());
        if (displayedUserList.isEmpty()) {
            tvEmpty.setText("Tidak ada pengguna ditemukan untuk kata kunci ini.");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        tvSectionTitle.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        if (isLoading) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void showData(boolean hasData) {
        recyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        tvEmpty.setVisibility(hasData ? View.GONE : View.VISIBLE);
        tvSectionTitle.setVisibility(View.VISIBLE); // Pastikan judul selalu terlihat
    }
}