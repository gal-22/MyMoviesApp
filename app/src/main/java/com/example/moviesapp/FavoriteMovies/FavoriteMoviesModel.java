package com.example.moviesapp.FavoriteMovies;

import android.content.Context;

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

public class FavoriteMoviesModel implements FavoriteMoviesContract.Model {
    private static final String TAG = "FavoriteMoviesModel";
    private final Context context;
    private FavoriteMoviesPresenter presenter;
    private RequestQueue requestQueue;

    public FavoriteMoviesModel(FavoriteMoviesPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void loadMovies() {
        // API endpoint for favorite movies
        String url = "http://10.0.2.2:8080/api/movies";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,  // No request body for GET
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            // Wrap the JSONArray inside a JSONObject
                            JSONObject wrapper = new JSONObject();
                            wrapper.put("movies", responseArray);
                            // Pass the wrapped JSONObject to the presenter
                            presenter.onLoadMoviesLoaded(null, wrapper);
                        } catch (JSONException e) {
                            try {
                                presenter.onLoadMoviesLoaded(e, null);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            presenter.onLoadMoviesLoaded(error, null);
                        } catch (JSONException e) {
                            try {
                                presenter.onLoadMoviesLoaded(new Exception("Error parsing JSON"), null);
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
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        // Add the request to the RequestQueue to execute it
        requestQueue.add(jsonArrayRequest);
    }
}