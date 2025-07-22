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
    private List<User> userList = new ArrayList<>(); // Inisialisasi list
    private EditText etSearch;
    private UserApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Inisialisasi ApiService
        apiService = ApiClient.getClient().create(UserApiService.class);

        // Setup RecyclerView dengan adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Panggil method untuk mengambil data dari API
        fetchRecipients();

        // Setup listener untuk search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
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
                    // Hapus data lama dan tambahkan data baru
                    userList.clear();
                    userList.addAll(response.body().getUsers());
                    // Beri tahu adapter bahwa data telah berubah
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchActivity.this, "Gagal memuat data penerima.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("SearchActivity", "onFailure: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}