package com.kasihinapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "KasihinAppPref";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Menyimpan token login ke SharedPreferences.
     */
    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.commit();
    }

    /**
     * Mengambil token login dari SharedPreferences.
     */
    public String getAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, null);
    }

    /**
     * Menghapus semua data session (untuk logout).
     */
    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}