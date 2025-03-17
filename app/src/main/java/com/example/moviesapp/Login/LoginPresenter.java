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
        if (error != null) {
            // Handle network or unexpected errors
            loginView.showError(error.getMessage());
            return;
        }

        try {
            // Check if the response contains a token (indicating a successful login)
            if (response.has("token")) {
                String token = response.getString("token");
                JSONObject user = response.getJSONObject("user");
                String username = user.getString("username");
                String email = user.getString("email");
                if (token != null && !token.isEmpty()) {
                    // Save the token using SessionManager
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.saveSession(token, username, email);
                    loginView.navigateToHomePage();
                } else {
                    // If token is empty, fall back to checking for an error message
                    String errorMsg = response.optString("message", "Login failed without a proper error message.");
                    loginView.showError(errorMsg);
                }
            } else if (response.has("message")) {
                // No token was returned, so extract and display the error message
                String errorMsg = response.getString("message");
                loginView.showError(errorMsg);
            } else {
                // Fallback error if neither token nor message is present
                loginView.showError("Unknown error occurred during login.");
            }
        } catch (JSONException e) {
            loginView.showError("Error parsing login response: " + e.getMessage());
        }
    }
}
