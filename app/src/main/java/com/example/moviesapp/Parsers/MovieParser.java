package com.example.moviesapp.Parsers;

import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieParser {
    public static ArrayList<Movie> parseMoviesTMDB(JSONArray jsonArray) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieJson = jsonArray.getJSONObject(i);

            // Create and populate the Movie object
            Movie movie = new Movie();

            movie.setRate(movieJson.optDouble("vote_average", 0.0));
            movie.setReleaseDate(movieJson.optString("release_date", ""));
            movie.setName(movieJson.optString("title", ""));
            movie.setDescription(movieJson.optString("overview", ""));
            // Parse the "genre_ids" which is another array inside the object
            List<Integer> genreIds = new ArrayList<>();
            JSONArray genreArray = movieJson.optJSONArray("genre_ids");
            if (genreArray != null) {
                for (int j = 0; j < genreArray.length(); j++) {
                    genreIds.add(genreArray.optInt(j));
                }
            }
            movie.setGenreIds(genreIds);

            movie.setPosterPath(movieJson.optString("poster_path", ""));
            movie.setBackdropPath(movieJson.optString("backdrop_path", ""));
            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<Movie> parseMovies(JSONArray jsonArray) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieJson = jsonArray.getJSONObject(i);

            Movie movie = new Movie();

            movie.setTMDBId(movieJson.optInt("tmdbId", 0));
            movie.setId(movieJson.optInt("id", 0));
            movie.setRate(movieJson.optDouble("voteAverage", 0.0));
            movie.setReleaseDate(movieJson.optString("releaseDate", ""));
            movie.setName(movieJson.optString("title", ""));
            movie.setDescription(movieJson.optString("overview", ""));
            movie.setPosterPath(movieJson.optString("posterPath", ""));
            movie.setBackdropPath(movieJson.optString("backdropPath", ""));

            // Boolean fields
            movie.setFavorite(movieJson.optBoolean("favorite", false));
            movie.setRented(movieJson.optBoolean("rented", false));
            movie.setRentedByUser(movieJson.optBoolean("rentedByUser", false));

            // Genre IDs
            List<Integer> genreIds = new ArrayList<>();
            JSONArray genreArray = movieJson.optJSONArray("genreIds");
            if (genreArray != null) {
                for (int j = 0; j < genreArray.length(); j++) {
                    genreIds.add(genreArray.optInt(j));
                }
            }
            movie.setGenreIds(genreIds);

            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<Movie> parseOrderHistoryMovies(JSONArray jsonArray) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieJson = jsonArray.getJSONObject(i);
            Movie movie = new Movie();

            movie.setTMDBId(movieJson.optInt("tmdbId", 0));
            movie.setId(movieJson.optInt("id", 0));
            movie.setRate(movieJson.optDouble("voteAverage", 0.0));
            movie.setReleaseDate(movieJson.optString("releaseDate", ""));
            movie.setName(movieJson.optString("title", ""));
            movie.setDescription(movieJson.optString("overview", ""));
            movie.setFavorite(movieJson.optBoolean("favorite", false));
            movie.setRented(movieJson.optBoolean("rented", false));
            movie.setRentedByUser(movieJson.optBoolean("rentedByUser", false));

            // Parse genreIds
            List<Integer> genreIds = new ArrayList<>();
            JSONArray genreArray = movieJson.optJSONArray("genreIds");
            if (genreArray != null) {
                for (int j = 0; j < genreArray.length(); j++) {
                    genreIds.add(genreArray.optInt(j));
                }
            }
            movie.setGenreIds(genreIds);

            movie.setPosterPath(movieJson.optString("posterPath", ""));
            movie.setBackdropPath(movieJson.optString("backdropPath", ""));

            movies.add(movie);
        }

        return movies;
    }
}
