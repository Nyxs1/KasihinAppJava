package com.kasihinapp.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kasihinapp.R;
import com.kasihinapp.search.UserAdapter;
import com.kasihinapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private UserAdapter adapter;
    private List<User> userList;
    private EditText etSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

// /search/SearchActivity.java

        userList = new ArrayList<>();

// Mengikuti struktur: new User(int id, String nama, String email, String role, int poin, String createdAt)
        userList.add(new User(1, "Elzrow", "elzrow@example.com", "Streamer", 150, "15.04 AM"));
        userList.add(new User(2, "Fahmi", "fahmi@example.com", "Gamer", 200, "10.00 PM"));
        userList.add(new User(3, "Dimas", "dimas@example.com", "Editor", 95, "8.00 AM"));
        userList.add(new User(4, "Salsa", "salsa@example.com", "Streamer", 300, "Yesterday"));

        adapter = new UserAdapter(userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
}
