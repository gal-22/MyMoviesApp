package com.example.moviesapp.CreateAccount;

import android.content.Context;
import android.util.Patterns;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.moviesapp.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Presenter class for handling business logic between View and Model
 */
public class CreateAccountPresenter implements CreateAccountContract.Presenter {

    private final CreateAccountContract.View createAccountView;
    private final CreateAccountContract.Model createAccountModel;
    private final Context context;

    public CreateAccountPresenter(CreateAccountContract.View view, Context context) {
        this.createAccountView = view;
        this.context = context;
        this.createAccountModel = new CreateAccountModel(this);
    }

    @Override
    public void onCreateAccountButtonClicked(String name, String email, String password) {
        // Validate input first
        boolean isValid = validateInput(name, email, password);

        if (isValid) {
            createAccountView.showProgress();
            createAccountModel.registerUser(name, email, password, context);
        }
    }

    @Override
    public void onSignUpLoadCompleted(@Nullable Exception e, @Nullable JSONObject response) {
        createAccountView.hideProgress();

        if (e != null) {
            // Handle exception
            if (e instanceof VolleyError) {
                VolleyError volleyError = (VolleyError) e;
                NetworkResponse networkResponse = volleyError.networkResponse;

                if (networkResponse != null && networkResponse.data != null) {
                    try {
                        String responseBody = new String(networkResponse.data);
                        JSONObject errorJson = new JSONObject(responseBody);
                        String errorMessage = errorJson.optString("message", "Registration failed");
                        createAccountView.showError(errorMessage);
                    } catch (JSONException jsonException) {
                        createAccountView.showError("Registration failed: " + volleyError.getMessage());
                    }
                } else {
                    createAccountView.showError("Network error: " + volleyError.getMessage());
                }
            } else {
                createAccountView.showError("Error: " + e.getMessage());
            }
            return;
        }

        else if (response != null) {
            try {
                // Check for success message or token in response
                String token = response.optString("token", null);
                String message = response.optString("message", "Account created successfully!");

                if (token != null) {
                    createAccountView.showSuccess(message);

                    String username = response.optString("username", "");
                    String email    = response.optString("email", "");

                    // Save the session & move to login
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.saveSession(token, username, email);
                    createAccountView.navigateToLogin();
                } else {
                    // Check if there's an error message
                    String errorMessage = response.optString("error", null);
                    if (errorMessage != null) {
                        createAccountView.showError(errorMessage);
                    } else {
                        createAccountView.showError("Account is already registered or created successfully, but no token was returned.");
                    }
                }
            } catch (Exception jsonException) {
                createAccountView.showError("Error processing response: " + jsonException.getMessage());
            }
        } else {
            createAccountView.showError("Empty response from server");
        }
    }

    /**
     * Validates user input for registration form
     */
    private boolean validateInput(String name, String email, String password) {
        boolean isValid = true;

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            createAccountView.showNameError("Name is required");
            isValid = false;
        } else {
            createAccountView.showNameError(null);
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            createAccountView.showEmailError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            createAccountView.showEmailError("Please enter a valid email address");
            isValid = false;
        } else {
            createAccountView.showEmailError(null);
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            createAccountView.showPasswordError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            createAccountView.showPasswordError("Password must be at least 6 characters");
            isValid = false;
        } else {
            createAccountView.showPasswordError(null);
        }

        return isValid;
    }
}