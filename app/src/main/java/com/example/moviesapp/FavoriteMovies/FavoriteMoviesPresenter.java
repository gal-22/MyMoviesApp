package com.example.moviesapp.FavoriteMovies;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.moviesapp.Parsers.MovieParser;
import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteMoviesPresenter implements FavoriteMoviesContract.Presenter {
    private FavoriteMoviesContract.View favoriteMoviesView;
    private FavoriteMoviesContract.Model favoriteMoviesModel;
    private ArrayList<Movie> favoriteMovies;

    public FavoriteMoviesPresenter(FavoriteMoviesContract.View favoriteMoviesView, Context context) {
        this.favoriteMoviesView = favoriteMoviesView;
        this.favoriteMoviesModel = new FavoriteMoviesModel(this, context);
        // Load favorite movies immediately
        this.favoriteMoviesModel.loadFavoriteMovies();
    }

    @Override
    public void onFavoriteMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException {
        if (e != null) {
            // Handle error
            favoriteMoviesView.hideProgress();
            favoriteMoviesView.showError("Error loading favorite movies: " + e.getMessage());
            return;
        }

        if (response != null) {
            // Parse the JSON response
            JSONArray moviesArray = response.optJSONArray("movies");
            if (moviesArray != null) {
                // Parse the movie objects using your MovieParser
                ArrayList<Movie> movies = MovieParser.parseMovies(moviesArray);
                this.favoriteMovies = movies;

                // Update the UI
                favoriteMoviesView.hideProgress();
                favoriteMoviesView.initializeRecyclerView(movies);
            } else {
                favoriteMoviesView.hideProgress();
                favoriteMoviesView.showError("No favorite movies found");
            }
        } else {
            favoriteMoviesView.hideProgress();
            favoriteMoviesView.showError("Empty response from server");
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Navigate to movie details when a movie is clicked
        favoriteMoviesView.navigateToMovieDetails(movie);
    }

    // Method to refresh favorites list
    public void refreshFavorites() {
        favoriteMoviesView.showProgress();
        favoriteMoviesModel.loadFavoriteMovies();
    }
}