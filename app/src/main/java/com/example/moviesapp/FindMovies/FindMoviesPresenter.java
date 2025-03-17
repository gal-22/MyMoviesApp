package com.example.moviesapp.FindMovies;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.stream.Collectors;
import com.example.moviesapp.Parsers.MovieParser;
import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindMoviesPresenter implements FindMoviesContract.Presenter{
    private FindMoviesContract.View findMoviesView;
    private FindMoviesContract.Model findMoviesModel;
    private ArrayList<Movie> movies;

    private ArrayList<Integer> selectedGenres = new ArrayList<>();

    public FindMoviesPresenter(FindMoviesContract.View findMoviesView, Context context) {
        this.findMoviesView = findMoviesView;
        this.findMoviesModel = new FindMoviesModel(this, context);
        this.findMoviesModel.loadMovies();
    }
    @Override
    public void onSearchViewTextChanged(String text) {
        if (text == null || text.isEmpty()) {
            // If search text is empty, show all movies
            findMoviesView.updateRecyclerView(movies);
        } else {
            // Search locally first (client-side filtering)
            String lowerCaseText = text.toLowerCase();

            ArrayList<Movie> filteredMovies = movies.stream()
                    .filter(movie -> movie.getName().toLowerCase().contains(lowerCaseText))
                    .collect(Collectors.toCollection(ArrayList::new));
            findMoviesView.updateRecyclerView(filteredMovies);
        }
    }

    @Override
    public void onMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException {
        if (response != null) {
            JSONArray arr = response.optJSONArray("movies");
            ArrayList<Movie> movies = MovieParser.parseMovies(arr != null ? arr : new JSONArray());
            this.movies = movies;
            findMoviesView.initializeRecyclerView(movies);
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        findMoviesView.navigateToMovieDetails(movie);
    }

    public void onGenreStatusChanged(int categoryId, boolean isChecked) {
        if (isChecked) {
            // Add to selected genre list
            selectedGenres.add(categoryId);

            // Filter movies that contain at least one of the selectedGenres
            ArrayList<Movie> filteredMovies = movies.stream()
                    .filter(m -> {
                        for (int genreId : m.getGenreIds()) {
                            if (selectedGenres.contains(genreId)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    // Collect into an ArrayList instead of a generic List
                    .collect(Collectors.toCollection(ArrayList::new));

            // Update the RecyclerView with filtered movies
            findMoviesView.updateRecyclerView(filteredMovies);
        } else {
            // Remove the unselected genre
            selectedGenres.remove(Integer.valueOf(categoryId));
            if (selectedGenres.isEmpty()) {
                findMoviesView.updateRecyclerView(movies);
            } else {
                // Re-filter the list according to remaining selected genres
                ArrayList<Movie> filteredMovies = movies.stream()
                        .filter(m -> {
                            for (int genreId : m.getGenreIds()) {
                                if (selectedGenres.contains(genreId)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .collect(Collectors.toCollection(ArrayList::new));

                findMoviesView.updateRecyclerView(filteredMovies);
            }
        }
    }
}
