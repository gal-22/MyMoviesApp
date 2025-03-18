package com.example.moviesapp.Login;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public interface LoginContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showLoginSuccess();
        void navigateToHomePage();

        void showError(String message);

        void navigateToSignUp();
    }

    interface Presenter {
        void onLoginButtonPressed(String username, String password); // Pass username and password directly
        void onLoginLoadCompleted(@Nullable Exception e, @Nullable JSONObject response);
    }

    interface Model {
        void login(String username, String password, Context context);
    }
}
