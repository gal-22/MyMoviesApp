package com.example.moviesapp.Login;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel implements LoginContract.Model {

    private LoginPresenter loginPresenter;
    public LoginModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void login(String username, String password, Context context) {
// Use 10.0.2.2 for localhost when running on an Android emulator
        String url = "http://10.0.2.2:8080/api/auth/login";

        // Build the JSON request body with the provided username and password
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            loginPresenter.onLoginLoadCompleted(e, null);
            return;
        }

        // Create the POST request using Volley
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Call the presenter's method with a successful response
                        loginPresenter.onLoginLoadCompleted(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call the presenter's method with the error
                        loginPresenter.onLoginLoadCompleted(error, null);
                    }
                }
        );

        // Create a new RequestQueue and add the request to execute it
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(loginRequest);
    }
}
