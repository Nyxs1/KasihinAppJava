package com.kasihinapp.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson; // <-- Tambahkan import ini
import com.kasihinapp.R;
import com.kasihinapp.model.User;
import com.kasihinapp.model.UserResponse;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.UserApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity"; // Tag untuk Logcat

    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private EditText etSearch;
    private UserApiService apiService;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        apiService = ApiClient.getClient().create(UserApiService.class);
        setupRecyclerView();
        fetchRecipients();
        setupSearchFilter();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);
    }

    private void fetchRecipients() {
        showLoading(true);
        Log.d(TAG, "Memulai panggilan API ke server...");

        apiService.getRecipients().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                showLoading(false);

                // --- LOG 1: Tampilkan JSON mentah dari server ---
//                if (response.body() != null) {
//                    Log.d(TAG, "Respons JSON mentah: " + new Gson().toJson(response.body()));
//                } else {
//                    Log.e(TAG, "Respons body kosong (null). Kode: " + response.code());
//                }
                // --------------------------------------------------

                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<User> receivedUsers = response.body().getUsers();
                    if (receivedUsers != null && !receivedUsers.isEmpty()) {
                        userList.clear();
                        userList.addAll(receivedUsers);
                        adapter.filter(""); // Refresh
                        showData(true);

                        // --- LOG 2: Tampilkan data yang sudah di-parsing ---
//                        Log.d(TAG, "Berhasil parsing data! Jumlah: " + userList.size());
//                        for (User user : userList) {
//                            Log.d(TAG, "User Diterima: ID=" + user.getId() + ", Nama=" + user.getNama() + ", Poin=" + user.getPoin());
//                        }
                        // ----------------------------------------------------

                    } else {
                        Log.d(TAG, "Respons berhasil, tapi daftar pengguna kosong.");
                        showData(false);
                    }
                } else {
                    Log.e(TAG, "Respons dari server tidak berhasil atau status=false.");
                    showData(false);
                    Toast.makeText(SearchActivity.this, "Gagal memuat data dari server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                showLoading(false);
                showData(false);
                Log.e(TAG, "Panggilan API gagal total (onFailure).", t);
                Toast.makeText(SearchActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ... sisa kode (setupSearchFilter, showLoading, showData) tetap sama ...
    private void setupSearchFilter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
    private void showData(boolean hasData) {
        recyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        tvEmpty.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }
}