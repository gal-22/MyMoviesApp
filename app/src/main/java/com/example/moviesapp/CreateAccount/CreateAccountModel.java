package com.example.moviesapp.CreateAccount;

import android.content.Context;
import android.util.Log;

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

/**
 * Model class for handling API communication for user registration
 */
public class CreateAccountModel implements CreateAccountContract.Model {

    private static final String TAG = "CreateAccountModel";
    private final CreateAccountContract.Presenter createAccountPresenter;

    public CreateAccountModel(CreateAccountContract.Presenter presenter) {
        this.createAccountPresenter = presenter;
    }

    @Override
    public void registerUser(String username, String email, String password, Context context) {
        String url = "http://10.0.2.2:8080/api/auth/register";

        // 1) Build JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            // JSON construction failed—report back immediately
            createAccountPresenter.onSignUpLoadCompleted(e, null);
            return;
        }

        // 2) Create the POST request
        JsonObjectRequest registerRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Got a 2xx response—forward it
                        createAccountPresenter.onSignUpLoadCompleted(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Non‑2xx or network error—forward it
                        createAccountPresenter.onSignUpLoadCompleted(error, null);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                // Ensure the body is sent as JSON
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Tell the server we want JSON back, and we’re sending JSON
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", getBodyContentType());
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // 3) Don’t cache this request (avoids any stale responses)
        registerRequest.setShouldCache(false);

        // 4) Fire it off
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(registerRequest);
    }
}