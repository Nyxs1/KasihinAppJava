package com.kasihinapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kasihinapp.model.LoginResponse;
import com.kasihinapp.utils.ApiClient;
import com.kasihinapp.utils.SessionManager;
import com.kasihinapp.utils.UserApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton loginButton;
    private ImageView backArrow;
    private TextView forgotPassword, textViewSignUp;
    private UserApiService apiService; // <-- Tambahkan variabel untuk service API
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        apiService = ApiClient.getClient().create(UserApiService.class);
        sessionManager = new SessionManager(this); // <-- Inisialisasi SessionManager

        initViews();
        setupListeners();
        validateInputs();
    }

    // ... (initViews dan setupListeners tetap sama) ...
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        backArrow = findViewById(R.id.back_arrow);
        forgotPassword = findViewById(R.id.forgot_password);
        textViewSignUp = findViewById(R.id.textViewSignUp);
    }
    private void setupListeners() {
        etEmail.addTextChangedListener(loginTextWatcher);
        etPassword.addTextChangedListener(loginTextWatcher);
        backArrow.setOnClickListener(v -> finish());
        loginButton.setOnClickListener(v -> validateAndLogin());
        forgotPassword.setOnClickListener(v ->
                Toast.makeText(EmailLoginActivity.this, "Forgot Password Clicked!", Toast.LENGTH_SHORT).show()
        );
        textViewSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(EmailLoginActivity.this, ActivitySignup1.class);
            startActivity(intent);
        });
    }

    // ... (loginTextWatcher dan validateInputs tetap sama) ...
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            validateInputs();
        }
    };
    private void validateInputs() {
        String emailInput = etEmail.getText().toString().trim();
        String passwordInput = etPassword.getText().toString().trim();
        if (!emailInput.isEmpty() && !passwordInput.isEmpty()) {
            loginButton.setEnabled(true);
            loginButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        }
    }


    /**
     * Method untuk memvalidasi dan melakukan login ke API.
     */
    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tampilkan loading (opsional, tapi sangat direkomendasikan)
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();

        Call<LoginResponse> call = apiService.login(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isStatus()) {
                        // Login berhasil
                        Toast.makeText(EmailLoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // TODO: Simpan token (loginResponse.getToken()) ke SharedPreferences
                        // --- INI BAGIAN PENTINGNYA ---
                        // Simpan token ke session
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        // --------------------------

                        // Pindah ke HomePage
                        Intent intent = new Intent(EmailLoginActivity.this, HomePage.class);
                        startActivity(intent);
                        finish(); // Tutup halaman login
                    } else {
                        // Login gagal dari server (misal: password salah)
                        Toast.makeText(EmailLoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Respons gagal dari server (error 404, 500, dll)
                    Toast.makeText(EmailLoginActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Gagal karena masalah jaringan atau lainnya
                Log.e("LoginActivity", "onFailure: " + t.getMessage());
                Toast.makeText(EmailLoginActivity.this, "Tidak ada koneksi internet atau server bermasalah.", Toast.LENGTH_LONG).show();
            }
        });
    }
}