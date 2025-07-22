package com.kasihinapp.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private EditText etSearch;
    private UserApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // --- PERBAIKAN 1: Gunakan layout yang benar ---
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        apiService = ApiClient.getClient().create(UserApiService.class);

        // Setup RecyclerView dan Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Ambil data dari API
        fetchRecipients();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Pastikan adapter tidak null sebelum memfilter
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchRecipients() {
        Toast.makeText(this, "Memuat data...", Toast.LENGTH_SHORT).show();
        Call<UserResponse> call = apiService.getRecipients();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    userList.clear();
                    userList.addAll(response.body().getUsers());

                    // --- PERBAIKAN 2: Beri tahu adapter untuk refresh ---
                    adapter.filter(""); // Memanggil filter untuk me-refresh data di adapter

                } else {
                    Toast.makeText(SearchActivity.this, "Gagal memuat data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("SearchActivity", "onFailure: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}