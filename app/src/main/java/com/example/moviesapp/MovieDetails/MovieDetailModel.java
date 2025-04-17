package com.example.moviesapp.MovieDetails;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MovieDetailModel implements MovieDetailContract.Model {

    private static final String TAG = "MovieDetailModel";
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

    @Override
    public void getRentalStatus() {
        // Get the user's rental history
        String url = "http://10.0.2.2:8080/api/rentals/history";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            boolean isRented = false;
                            String returnDate = null;

                            // Loop through rentals to find the current movie
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rental = response.getJSONObject(i);
                                JSONObject movieObj = rental.getJSONObject("movie");

                                if (movieObj.getLong("id") == movie.getId()) {
                                    // Check if return date is null (still rented)
                                    if (rental.isNull("returnDate")) {
                                        isRented = true;
                                        break;
                                    }
                                }
                            }

                            // Notify presenter about rental status
                            presenter.onRentalStatusLoaded(isRented);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing rental history", e);
                            presenter.onError("Error checking rental status: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Network error getting rental history", error);
                        presenter.onError("Network error checking rental status");
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
    public void rentMovie() {
        // Use the POST /api/rentals/{movieId} endpoint to rent a movie
        String url = "http://10.0.2.2:8080/api/rentals/rent/" + movie.getId();


        // Create a new request, matching the pattern of changeMovieFavoriteStatus which works
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,  // No request body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.optString("message", "Movie rented successfully");
                            boolean success = !message.toLowerCase().contains("error");

                            // Notify presenter about rental result
                            presenter.onRentalCompleted(success, message);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing rental response", e);
                            presenter.onError("Error processing rental: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Network error renting movie", error);

                        // Get error message
                        String errorMessage = "Network error renting movie";
                        if (error.networkResponse != null) {
                            errorMessage += " (Status code: " + error.networkResponse.statusCode + ")";
                        }

                        presenter.onError(errorMessage);
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

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    public void returnMovie() {
        // Use the POST /api/rentals/return/{movieId} endpoint to return a movie
        String url = "http://10.0.2.2:8080/api/rentals/return/" + movie.getId();

        // Create a new request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,  // No request body
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.optString("message", "Movie returned successfully");
                            boolean success = !message.toLowerCase().contains("error");

                            // Notify presenter about return result
                            presenter.onReturnCompleted(success, message);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing return response", e);
                            presenter.onError("Error processing return: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Network error returning movie", error);

                        String errorMessage = "Network error returning movie";
                        if (error.networkResponse != null) {
                            errorMessage += " (Status code: " + error.networkResponse.statusCode + ")";
                        }

                        presenter.onError(errorMessage);
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

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}