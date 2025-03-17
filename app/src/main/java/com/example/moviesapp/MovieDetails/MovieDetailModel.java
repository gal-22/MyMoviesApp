package com.example.moviesapp.MovieDetails;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.MovieDetails.MovieDetailContract;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MovieDetailModel implements MovieDetailContract.Model {

    private Movie movie;
    private Context context;
    private RequestQueue requestQueue;
    private MovieDetailContract.Presenter presenter;

    public MovieDetailModel(Context context, Movie movie, MovieDetailContract.Presenter presenter) {
        this.context = context;
        this.movie = movie;
        this.presenter = presenter;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void getFavoriteStatus() {
        // Use the GET /api/favorites endpoint to get all favorites
        String url = "http://10.0.2.2:8080/api/favorites";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Check if the current movie ID is in the favorites list
                            boolean isFavorite = false;
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject movieObj = response.getJSONObject(i);
                                long id = movieObj.getLong("id");
                                if (id == movie.getId()) {
                                    isFavorite = true;
                                    break;
                                }
                            }
                            presenter.onGetFavoriteStatus(isFavorite);
                        } catch (JSONException e) {
                            // Call the error method on the presenter
                            presenter.onError("Error parsing favorites: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call the error method on the presenter
                        presenter.onError("Network error getting favorites: " +
                                (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    }
                }
        ) {
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

        requestQueue.add(request);
    }

    @Override
    public void changeMovieFavoriteStatus() {
        // Use the POST /api/favorites/{movieId} endpoint to toggle favorite status
        String url = "http://10.0.2.2:8080/api/favorites/" + this.movie.getId();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // The response should have a message indicating what happened
                            String message = response.getString("message");

                            // Determine the new status based on the message
                            boolean newStatus;
                            if (message.contains("added") || message.contains("Added")) {
                                newStatus = true;
                            } else if (message.contains("removed") || message.contains("Removed")) {
                                newStatus = false;
                            } else {
                                // If we can't determine from the message, report an error
                                presenter.onError("Couldn't determine favorite status from response: " + message);
                                return;
                            }

                            // Directly call the presenter method
                            presenter.onFinishedChangingFavoriteStatus(newStatus);
                        } catch (JSONException e) {
                            // Call the error method on the presenter
                            presenter.onError("Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Call the error method on the presenter
                        presenter.onError("Network error toggling favorite: " +
                                (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SessionManager sessionManager = new SessionManager(context);
                String token = sessionManager.getAuthToken();
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }
}