package com.example.moviesapp.OrderHistory;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderHistoryModel implements OrderHistoryContract.Model {
    private static final String TAG = "OrderHistoryModel";
    private final Context context;
    private OrderHistoryPresenter presenter;
    private RequestQueue requestQueue;

    public OrderHistoryModel(OrderHistoryPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void loadOrderHistory() {
        // API endpoint for rental history
        String url = "http://10.0.2.2:8080/api/rentals/history/movies";
        Log.d(TAG, "Loading rental history from: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,  // No request body for GET
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            Log.d(TAG, "Received " + responseArray.length() + " rental history items");

                            // Wrap the JSONArray inside a JSONObject
                            JSONObject wrapper = new JSONObject();
                            wrapper.put("rentals", responseArray);

                            // Pass the wrapped JSONObject to the presenter
                            presenter.onOrderHistoryLoaded(null, wrapper);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing rental history", e);
                            try {
                                presenter.onOrderHistoryLoaded(e, null);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error loading rental history", error);

                        String errorMessage = "Error loading rental history";
                        if (error instanceof AuthFailureError) {
                            errorMessage = "Authentication error. Please log in again.";
                        } else if (error.networkResponse != null) {
                            errorMessage += " (Status: " + error.networkResponse.statusCode + ")";
                        }

                        Log.e(TAG, errorMessage, error);

                        try {
                            presenter.onOrderHistoryLoaded(error, null);
                        } catch (JSONException e) {
                            try {
                                presenter.onOrderHistoryLoaded(new Exception(errorMessage), null);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
        ) {
            // Add Authorization header with JWT token from SessionManager
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SessionManager sessionManager = new SessionManager(context);
                String token = sessionManager.getAuthToken();

                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                    Log.d(TAG, "Added auth token to request");
                } else {
                    Log.w(TAG, "No auth token available for request");
                }

                return headers;
            }
        };

        // Add the request to the RequestQueue to execute it
        requestQueue.add(jsonArrayRequest);
    }
}