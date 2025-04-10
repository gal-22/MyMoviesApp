package com.example.moviesapp.FindMovies;

import androidx.annotation.Nullable;

import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public interface FindMoviesContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void initializeRecyclerView(ArrayList<Movie> movies);
        void updateRecyclerView(ArrayList<Movie> movies);

        void navigateToMovieDetails(Movie movie);
    }

    interface Presenter {
        // View Events
        void onSearchViewTextChanged(String text);

        // Model Events
        void onMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException;

        void onMovieClick(Movie movie);
    }

    interface Model {
        void loadMoviesTMDB();

        void loadMovies();

        void searchMovies(String text);
    }

}
