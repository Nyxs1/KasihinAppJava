package com.kasihinapp.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.auth0.android.jwt.JWT;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.kasihinapp.search.activity_berhasil;
import com.kasihinapp.R;
import com.kasihinapp.model.DonationResponse;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.SessionManager;
import com.kasihinapp.utils.UserApiService;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonasiActivity extends AppCompatActivity {

    // ... (deklarasi variabel tetap sama)
    private Toolbar toolbar;
    private CircleImageView ivPenerimaProfile;
    private TextView tvPenerimaDisplayName;
    private TextInputEditText etJumlahPoin;
    private Chip chip20, chip50, chip100, chip200;
    private EditText etPesanPengirim;
    private Button btnKirimPoin;

    private int idUserPenerima;
    private String namaUserPenerima;
    private int idUserPengirim;

    private UserApiService apiService;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);

        initViews();

        apiService = ApiClient.getClient().create(UserApiService.class);
        sessionManager = new SessionManager(this);

        idUserPengirim = getUserIdFromToken();
        idUserPenerima = getIntent().getIntExtra("user_id", -1);
        namaUserPenerima = getIntent().getStringExtra("user_name");

        toolbar.setTitle("Kirim Poin untuk " + namaUserPenerima);
        tvPenerimaDisplayName.setText(namaUserPenerima.toUpperCase());

        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarDonasi);
        ivPenerimaProfile = findViewById(R.id.ivProfile);
        tvPenerimaDisplayName = findViewById(R.id.tvDisplayName);
        etJumlahPoin = findViewById(R.id.etJumlahPoin);
        chip20 = findViewById(R.id.chip20);
        chip50 = findViewById(R.id.chip50);
        chip100 = findViewById(R.id.chip100);
        chip200 = findViewById(R.id.chip200);
        etPesanPengirim = findViewById(R.id.etPesanPengirim);
        btnKirimPoin = findViewById(R.id.btnKirimPoin);
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        chip20.setOnClickListener(v -> etJumlahPoin.setText("20"));
        chip50.setOnClickListener(v -> etJumlahPoin.setText("50"));
        chip100.setOnClickListener(v -> etJumlahPoin.setText("100"));
        chip200.setOnClickListener(v -> etJumlahPoin.setText("200"));

        btnKirimPoin.setOnClickListener(v -> {
            if (validateInputs()) {
                kirimDonasiKeApi();
            }
        });
    }

    private void kirimDonasiKeApi() {
        int jumlahPoin = Integer.parseInt(etJumlahPoin.getText().toString());
        String pesan = etPesanPengirim.getText().toString();

        Toast.makeText(this, "Mengirim " + jumlahPoin + " Poin...", Toast.LENGTH_SHORT).show();
        btnKirimPoin.setEnabled(false);

        Call<DonationResponse> call = apiService.sendUserDonation(idUserPengirim, idUserPenerima, jumlahPoin, pesan);
        call.enqueue(new Callback<DonationResponse>() {
            @Override
            public void onResponse(Call<DonationResponse> call, Response<DonationResponse> response) {
                btnKirimPoin.setEnabled(true);

                // --- BAGIAN INI DIPERBAIKI ---
                if (response.isSuccessful() && response.body() != null) {
                    DonationResponse donationResponse = response.body();

                    // Tampilkan pesan dari server, apapun itu (sukses atau gagal)
                    Toast.makeText(DonasiActivity.this, donationResponse.getMessage(), Toast.LENGTH_LONG).show();

                    // Jika status dari server adalah true, baru pindah halaman
                    if (donationResponse.isStatus()) {
                        Intent intent = new Intent(DonasiActivity.this, activity_berhasil.class);
                        startActivity(intent);
                        finish();
                    }
                    // Jika status false (seperti poin tidak cukup), tidak akan pindah halaman
                    Toast.makeText(DonasiActivity.this, donationResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Ini untuk error HTTP seperti 404, 500, dll.
                    Toast.makeText(DonasiActivity.this, "Gagal mengirim donasi. Terjadi kesalahan server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationResponse> call, Throwable t) {
                btnKirimPoin.setEnabled(true);
                Toast.makeText(DonasiActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ... (method validateInputs dan getUserIdFromToken tetap sama) ...
    private boolean validateInputs() {
        String poinStr = etJumlahPoin.getText().toString().trim();
        if (TextUtils.isEmpty(poinStr) || Integer.parseInt(poinStr) < 20) {
            etJumlahPoin.setError("Minimal donasi adalah 20 Poin");
            etJumlahPoin.requestFocus();
            return false;
        }
        return true;
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
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }
}