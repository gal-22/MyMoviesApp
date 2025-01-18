package com.example.moviesapp.Login;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    private LoginContract.Model loginModel;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel(this);
    }

    @Override
    public void onLoginButtonPressed(String username, String password) {
        loginView.showProgress();
        loginModel.login(username, password);
    }

    @Override
    public void onLoginLoadCompleted(@Nullable Exception e, @Nullable JSONObject response) {
        loginView.hideProgress();
        if (e != null) {
            loginView.showError(e.getMessage());
        } else if (response != null) {
            try {
                if (response.getBoolean("success")) {
                    loginView.navigateToHomePage();
                } else {
                    loginView.showError(response.getString("message"));
                }
            } catch (JSONException ex) {
                loginView.showError("Error parsing response");
            }
        }
    }
}
