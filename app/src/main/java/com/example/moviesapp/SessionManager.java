package com.example.moviesapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "app_session";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save token, username, and email together
    public void saveSession(String token, String username, String email) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Retrieve the JWT token
    public String getAuthToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    // Retrieve the username
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    // Retrieve the email
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    // Clear the session (e.g., on logout)
    public void clearSession() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
