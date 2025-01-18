package com.example.moviesapp.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel implements LoginContract.Model {

    private LoginPresenter loginPresenter;
    public LoginModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void login(String username, String password) {
        // TODO implement request to server to login
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true); // Add a "success" key with a value of true
            loginPresenter.onLoginLoadCompleted(null, jsonObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
