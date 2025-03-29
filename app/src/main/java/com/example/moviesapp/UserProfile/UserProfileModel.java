package com.example.moviesapp.UserProfile;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileModel implements UserProfileContract.Model {
    private static final String TAG = "UserProfileModel";
    private final Context context;
    private UserProfilePresenter presenter;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;

    public UserProfileModel(UserProfilePresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public void getUserProfile() {
        presenter.onProfileLoaded(sessionManager.getUsername(), sessionManager.getEmail());
    }

    @Override
    public void logout() {
        // Clear local session data first
        sessionManager.clearSession();

        // Optionally call logout endpoint on server
        String url = "http://10.0.2.2:8080/api/auth/logout";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Session already cleared locally, nothing more to do
                        Log.d(TAG, "Logged out successfully on server");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Still consider logout successful since we cleared local session
                        Log.e(TAG, "Error logging out on server: " +
                                (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sessionManager.getAuthToken();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        // Send the request but don't wait for response
        requestQueue.add(request);
    }
}