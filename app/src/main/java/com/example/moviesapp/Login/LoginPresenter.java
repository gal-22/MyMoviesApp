package com.example.moviesapp.Login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.moviesapp.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    private LoginContract.Model loginModel;
    private Context context;

    public LoginPresenter(LoginContract.View loginView, Context context) {
        this.loginView = loginView;
        this.loginModel = new LoginModel(this);
        this.context = context;
    }

    @Override
    public void onLoginButtonPressed(String username, String password) {
        loginView.showProgress();
        loginModel.login(username, password, context);
    }

    @Override
    public void onLoginLoadCompleted(Exception error, JSONObject response) {
        // First, hide your loading indicator
        loginView.hideProgress();

        if (error != null) {
            // Network or unexpected error
            loginView.showError(error.getMessage());
            return;
        }

        try {
            // Check for token success
            if (response.has("token")) {
                String token = response.getString("token");
                JSONObject user = response.getJSONObject("user");
                String username = user.getString("username");
                String email    = user.getString("email");

                // Save session and navigate
                SessionManager sessionManager = new SessionManager(context);
                sessionManager.saveSession(token, username, email);
                loginView.navigateToHomePage();

            } else if (response.has("message")) {
                // Server-side “bad credentials” or other msg
                loginView.showError(response.getString("message"));
            } else {
                // Fallback
                loginView.showError("Unknown error occurred during login.");
            }
        } catch (JSONException e) {
            loginView.showError("Error parsing login response: " + e.getMessage());
        }
    }
}
