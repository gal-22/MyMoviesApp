package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;

import java.util.ArrayList;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public FavoriteMoviesAdapter(Context context, ArrayList<Movie> movies, OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_favorite_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        // Set movie name
        holder.movieNameTextView.setText(movie.getName());

        // Set release year
        holder.movieYearTextView.setText(movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 4
                ? movie.getReleaseDate().substring(0, 4) : "");

        // Set rating
        holder.movieRatingTextView.setText(String.valueOf(movie.getRate()));

        // Load poster image using Glide
        String posterUrl = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
        Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.movieImageView);

        // Set the favorite button to filled (since this is already a favorite)
        holder.favoriteButton.setImageResource(R.drawable.heart_filled);

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public void updateMovies(ArrayList<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImageView;
        TextView movieNameTextView;
        TextView movieYearTextView;
        TextView movieRatingTextView;
        ImageView starImageView;
        ImageButton favoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.movieFavoriteImageView);
            movieNameTextView = itemView.findViewById(R.id.movieNameFavoriteTextView);
            movieYearTextView = itemView.findViewById(R.id.movieFavoriteYearTextView);
            movieRatingTextView = itemView.findViewById(R.id.movieFavoriteRatingTextView);
            starImageView = itemView.findViewById(R.id.favoriteMovieStartImageView);
            favoriteButton = itemView.findViewById(R.id.favoriteImageButton);
        }
    }
}