package com.example.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // If you want to use Glide
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;   // Make sure this matches your package

import java.util.ArrayList;
import java.util.List;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener listener;


    // Constructor
    public MoviesRecyclerViewAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    public void updateMovies(ArrayList<Movie> movies) {
        this.movieList = movies;
        this.notifyDataSetChanged();
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.movie_slot, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Movie name
        holder.movieName.setText(movie.getName());

        // Extract just the year from releaseDate if you need to show only year
        String releaseDate = movie.getReleaseDate();
        String year = "";
        if (releaseDate != null && releaseDate.length() >= 4) {
            year = releaseDate.substring(0, 4);
        }
        holder.movieYear.setText(year);

        // Movie rate
        holder.movieRate.setText(String.valueOf(movie.getRate()));

        // Movie description
        holder.movieDescription.setText(movie.getDescription());
        holder.movieSlotConstraints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieClick(movie);
            }
        });
        // Movie poster (if you have a valid URL in posterPath)
        // Here, we're using Glide to load an image from URL
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return (movieList != null) ? movieList.size() : 0;
    }

    // ========== INNER VIEWHOLDER CLASS ==========
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieName, movieYear, movieRate, movieDescription;
        ConstraintLayout movieSlotConstraints;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movieSlotPosterIv);
            movieName = itemView.findViewById(R.id.movieSlotNameTv);
            movieYear = itemView.findViewById(R.id.movieSlotYearTv);
            movieRate = itemView.findViewById(R.id.MovieSlotRankTv);
            movieDescription = itemView.findViewById(R.id.MovieSlotDescriptionTv);
            movieSlotConstraints = itemView.findViewById(R.id.movieSlotConstraints);
        }
    }
}