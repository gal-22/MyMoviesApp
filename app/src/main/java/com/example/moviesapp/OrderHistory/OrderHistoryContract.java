package com.example.moviesapp.OrderHistory;

import androidx.annotation.Nullable;

import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public interface OrderHistoryContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void initializeRecyclerView(ArrayList<Movie> movies);
        void updateRecyclerView(ArrayList<Movie> movies);
        void showEmptyState();
        void hideEmptyState();
        void navigateToMovieDetails(Movie movie);

    }

    interface Presenter {
        // Model Events
        void onOrderHistoryLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException;

        // View Events
        void refreshOrderHistory();

        void onMovieClick(Movie movie);
    }

    interface Model {
        void loadOrderHistory();
    }
}