package com.example.moviesapp.Login;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginModel implements LoginContract.Model {

    private LoginPresenter loginPresenter;

    public LoginModel(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void login(String username, String password, Context context) {
        String url = "http://10.0.2.2:8080/api/auth/login";

        // Build the JSON request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            loginPresenter.onLoginLoadCompleted(e, null);
            return;
        }

        // Create the POST request
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Success! forward to presenter
                        loginPresenter.onLoginLoadCompleted(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error! forward to presenter
                        loginPresenter.onLoginLoadCompleted(error, null);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Tell the server this is JSON
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Dispatch the request
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(loginRequest);
    }
}