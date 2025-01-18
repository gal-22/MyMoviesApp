package com.example.moviesapp.FindMovies;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.Parsers.MovieParser;
import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    public void loadMovies() {
        // If you need to notify a Presenter when movies are loaded,
        // you'll need a callback method with a listener parameter, e.g.:
        // public void loadMovies(OnMoviesLoadedListener listener) { ... }

        // For demonstration, here is a basic example that just logs and shows a Toast.
        // Replace "YOUR_API_KEY" with your actual TMDB v3 API key.
        String url = "https://api.themoviedb.org/3/movie/now_playing"
                + "?api_key=" + apiKey  // <-- Replace with your real key
                + "&language=en-US&page=1";

        // 1) Create a RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // 2) Build the JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,  // No request body for GET
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // This method runs on the main thread
                        JSONArray results = response.optJSONArray("results");
                        try {
                            if (results != null) {
                                List<Movie> movies = MovieParser.parseMovies(results);
                                Log.d(TAG, "onResponse: " + movies);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // If you have a callback, call listener.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        // If you have a callback, call listener.onError(error.toString());
                    }
                }
        );

        // 3) Add request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void searchMovies(String text) {
        // Similar approach:
        // 1) Construct your search URL
        // 2) Make a Volley request
        // 3) Handle response (call success/error callback)
    }
}
