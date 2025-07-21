package com.kasihinapp;

import android.content.Intent;
import android.content.res.ColorStateList; // Import ColorStateList
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher; // Import TextWatcher
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import ContextCompat

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EmailLoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton loginButton;
    private ImageView backArrow;
    private TextView forgotPassword, textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        initViews();
        setupListeners();

        // Panggil validasi sekali di awal untuk mengatur kondisi tombol
        validateInputs();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        backArrow = findViewById(R.id.back_arrow);
        forgotPassword = findViewById(R.id.forgot_password);
        textViewSignUp = findViewById(R.id.textViewSignUp);
    }

    private void setupListeners() {
        // --- PERUBAHAN 1: Tambahkan TextWatcher untuk validasi real-time ---
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

    // --- PERUBAHAN 2: Buat TextWatcher untuk memanggil validasi ---
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

    // --- PERUBAHAN 3: Buat method validasi untuk tombol ---
    private void validateInputs() {
        String emailInput = etEmail.getText().toString().trim();
        String passwordInput = etPassword.getText().toString().trim();

        if (!emailInput.isEmpty() && !passwordInput.isEmpty()) {
            // Jika kedua field terisi, aktifkan tombol dan ubah warna jadi hijau
            loginButton.setEnabled(true);
            loginButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        } else {
            // Jika salah satu field kosong, nonaktifkan tombol dan ubah warna jadi abu-abu
            loginButton.setEnabled(false);
            loginButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        }
    }

    private void validateAndLogin() {
        // Validasi ini tetap berguna sebagai pengecekan terakhir saat tombol diklik
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
    }
}