package com.kasihinapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.kasihinapp.R;

public class DonasiActivity extends AppCompatActivity {

    private TextView tvNama, tvPoin;
    private ImageView ivProfile, btnBack;
    private EditText etNama, etEmail, etPesan;
    private Button btnDonasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);

        tvNama = findViewById(R.id.tvDonasiNama);
        ivProfile = findViewById(R.id.ivDonasiProfile);
        tvPoin = findViewById(R.id.tvPoin);
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPesan = findViewById(R.id.etPesan);
        btnDonasi = findViewById(R.id.btnKirimDonasi);
        btnBack = findViewById(R.id.btnBack);

        String nama = getIntent().getStringExtra("name");
        int imageRes = getIntent().getIntExtra("image", R.mipmap.ic_launcher);

        tvNama.setText(nama);
        ivProfile.setImageResource(imageRes);
        tvPoin.setText("60"); // default poin

        btnDonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonasiActivity.this, Berhasil.class);
                startActivity(intent);
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
