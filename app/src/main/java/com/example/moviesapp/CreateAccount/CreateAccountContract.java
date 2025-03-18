package com.example.moviesapp.CreateAccount;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public interface CreateAccountContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void showSuccess(String message);
        void navigateToLogin();

        // Form validation helpers
        void showNameError(String error);
        void showEmailError(String error);
        void showPasswordError(String error);
    }

    interface Presenter {
        void onCreateAccountButtonClicked(String name, String email, String password);
        void onSignUpLoadCompleted(@Nullable Exception e, @Nullable JSONObject response);
    }

    interface Model {
        void registerUser(String name, String email, String password, Context context);
    }
}