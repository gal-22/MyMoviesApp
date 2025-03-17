package com.example.moviesapp.MovieDetails;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.moviesapp.FindMovies.FindMoviesFragment;
import com.example.moviesapp.FindMovies.FindMoviesPresenter;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {


    // These arguments are placeholders if you need them for other things.
    // You can remove them if you don't need them.
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Key used to retrieve the Movie from the Bundle
    // (either from Safe Args or manually).
    private static final String ARG_MOVIE = "movie";

    private Movie movie;
    private View rootView;

    private ImageView posterImageView;
    private TextView movieNameTextView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieDescriptionTextView;
    private Button orderMovieButton;
    private FloatingActionButton favoriteMovieButton;

    private MovieDetailPresenter movieDetailPresenter;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters. If you are passing the Movie via Safe Args
     * or directly via a Bundle, you may not need these parameters.
     */
    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * If you are manually creating the fragment and passing the Movie object in code,
     * you can do it like this instead of the above factory method:
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the fragment arguments
        if (getArguments() != null) {
            // If you used newInstance(param1, param2)
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            // If you put the Movie object directly in the Bundle
            // (or if you used Safe Args, you would do something like
            // MovieDetailFragmentArgs.fromBundle(getArguments()).getMovie())
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment one time, keep it in rootView
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieDetailPresenter = new MovieDetailPresenter(MovieDetailFragment.this, getContext(), movie);
        // Find your views
        posterImageView = rootView.findViewById(R.id.detailedMovieBackdropImageView);
        movieNameTextView = rootView.findViewById(R.id.detailedMovieNameTextView);
        movieRatingTextView = rootView.findViewById(R.id.detailedMovieRatingTextView);
        movieReleaseDateTextView = rootView.findViewById(R.id.detailedMovieYearTextView);
        movieDescriptionTextView = rootView.findViewById(R.id.detailedMovieDescriptionTextView);
        orderMovieButton = rootView.findViewById(R.id.detailedMovieOrderMovieButton);
        favoriteMovieButton = rootView.findViewById(R.id.deatiledMovieFavoriteFloatingButton);
        favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDetailPresenter.onFavoriteButtonClick();
            }
        });
        // Populate the views if we have a valid Movie object
        if (movie != null) {
            String backdropUrl = "https://image.tmdb.org/t/p/original" + movie.getBackdropPath();
            Glide.with(this)
                    .load(backdropUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(posterImageView);

            movieNameTextView.setText(movie.getName());
            movieRatingTextView.setText(String.valueOf(movie.getRate()));
            movieReleaseDateTextView.setText(movie.getReleaseDate());
            movieDescriptionTextView.setText(movie.getDescription());
        }

        // Return the inflated and populated view
        return rootView;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setFavoriteButton(Boolean isFilled) {
        favoriteMovieButton.setImageResource(isFilled ? R.drawable.heart_filled : R.drawable.heart_unfilled);
    }
}
