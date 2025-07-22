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
import com.kasihinapp.search.Berhasil;
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

    // Deklarasi View disesuaikan dengan XML
    private Toolbar toolbar;
    private CircleImageView ivPenerimaProfile;
    private TextView tvPenerimaDisplayName;
    private TextInputEditText etJumlahPoin;
    private Chip chip20, chip50, chip100, chip200;
    private EditText etPesanPengirim;
    private Button btnKirimPoin;

    private int idUserPenerima;
    private String namaUserPenerima;
    private int idUserPengirim; // ID pengguna yang sedang login

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
        btnKirimPoin.setEnabled(false); // Nonaktifkan tombol selama proses

        Call<DonationResponse> call = apiService.sendUserDonation(idUserPengirim, idUserPenerima, jumlahPoin, pesan);
        call.enqueue(new Callback<DonationResponse>() {
            @Override
            public void onResponse(Call<DonationResponse> call, Response<DonationResponse> response) {
                btnKirimPoin.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DonasiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().isStatus()) {
                        Intent intent = new Intent(DonasiActivity.this, Berhasil.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(DonasiActivity.this, "Gagal mengirim donasi. Coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationResponse> call, Throwable t) {
                btnKirimPoin.setEnabled(true);
                Toast.makeText(DonasiActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

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