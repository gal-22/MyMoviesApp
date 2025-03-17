package com.example.moviesapp.FindMovies;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindMoviesModel implements FindMoviesContract.Model{
    private static final String TAG = "FindMoviesModel";
    private final Context context;
    private FindMoviesPresenter presenter;
    private final String baseUrl = "https://api.themoviedb.org/3";
    private final String apiKey = "ba50009df309cfd8d537ba914557af7f";
    private final String apiReadAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYTUwMDA5ZGYzMDljZmQ4ZDUzN2JhOTE0NTU3YWY3ZiIsIm5iZiI6MTUyNDA2MzExO" +
            "C43NTIsInN1YiI6IjVhZDc1YjhlYzNhMzY4NDgwYjAwZmNhNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JdAHKiV8u6k26tKAevcGjAUDRAnIcLhetzrTqZw2_j4";
    // Optionally pass in Context for creating the Volley RequestQueue
    public FindMoviesModel(FindMoviesPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        loadMovies();
    }

    @Override
    public void loadMoviesTMDB() {
        // If you need to notify a Presenter when movies are loaded,
        // you'll need a callback method with a listener parameter, e.g.:
        // public void loadMovies(OnMoviesLoadedListener listener) { ... }

        // For demonstration, here is a basic example that just logs and shows a Toast.
        // Replace "YOUR_API_KEY" with your actual TMDB v3 API key.
        String tmdbURL = "https://api.themoviedb.org/3/movie/now_playing"
                + "?api_key=" + apiKey  // <-- Replace with your real key
                + "&language=en-US&page=1";

        // 1) Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // 2) Build the JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                tmdbURL,
                null,  // No request body for GET
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            presenter.onMoviesLoaded(null, response);
                        } catch (JSONException e) {
                            try {
                                presenter.onMoviesLoaded(e, null);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        // 3) Add request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void loadMovies() {
        // Local server endpoint for movies (using 10.0.2.2 for emulator access to localhost)
        String url = "http://10.0.2.2:8080/api/movies";

        // Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Build the JsonArrayRequest to handle a JSON array response from your server
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
                            presenter.onMoviesLoaded(null, wrapper);
                        } catch (JSONException e) {
                            try {
                                presenter.onMoviesLoaded(e, null);
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
                            presenter.onMoviesLoaded(error, null);
                        } catch (JSONException e) {
                            try {
                                presenter.onMoviesLoaded(new Exception("Error parsing JSON"), null);
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

    @Override
    public void searchMovies(String text) {
        // Similar approach:
        // 1) Construct your search URL
        // 2) Make a Volley request
        // 3) Handle response (call success/error callback)
    }
}
