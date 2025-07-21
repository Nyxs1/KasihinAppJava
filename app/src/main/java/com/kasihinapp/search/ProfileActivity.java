package com.kasihinapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kasihinapp.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageView, btnBack;
    private TextView tvName, tvRole, tvBio;
    private Button btnDonasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = findViewById(R.id.imageViewProfile);
        tvName = findViewById(R.id.tvName);
        tvRole = findViewById(R.id.tvRole);
        tvBio = findViewById(R.id.tvBio);
        btnDonasi = findViewById(R.id.btnDonasi);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data dari intent
        String name = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        int imageRes = getIntent().getIntExtra("image", R.mipmap.ic_launcher);

        tvName.setText(name);
        tvRole.setText(role);
        tvBio.setText("--"); // bisa ubah nanti
        imageView.setImageResource(imageRes);

        btnDonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, DonasiActivity.class);
                i.putExtra("name", name);
                i.putExtra("image", imageRes);
                startActivity(i);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // kembali ke halaman sebelumnya
            }
        });
    }
}
