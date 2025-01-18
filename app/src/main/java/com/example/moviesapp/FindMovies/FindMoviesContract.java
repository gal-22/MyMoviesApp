package com.example.moviesapp.FindMovies;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public interface FindMoviesContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
    }

    interface Presenter {
        // View Events
        void onSearchViewTextChanged(String text);

        // Model Events
        void onMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response);
    }

    interface Model {
        void loadMovies();
        void searchMovies(String text);
    }

}
