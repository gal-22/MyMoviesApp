package com.example.moviesapp.FavoriteMovies;

import androidx.annotation.Nullable;

import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public interface FavoriteMoviesContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void initializeRecyclerView(ArrayList<Movie> movies);
        void updateRecyclerView(ArrayList<Movie> movies);

        void navigateToMovieDetails(Movie movie);
    }

    interface Presenter {

        // Model Events
        void onLoadMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException;

        void onMovieClick(Movie movie);
    }

    interface Model {
        void loadMovies();
    }
}
