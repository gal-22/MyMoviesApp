package com.example.moviesapp.MovieDetails;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Movie detail fragment showing movie information and rental options
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_MOVIE = "movie";

    private String mParam1;
    private String mParam2;
    private Movie movie;
    private View rootView;

    // UI components
    private ImageView posterImageView;
    private TextView movieNameTextView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieDescriptionTextView;
    private Button orderMovieButton;
    private TextView rentalStatusTextView;
    private ProgressBar progressBar;
    private FloatingActionButton favoriteMovieButton;

    private MovieDetailPresenter movieDetailPresenter;

    private TextView movieDateTextView;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Find all views
        posterImageView = rootView.findViewById(R.id.detailedMovieBackdropImageView);
        movieNameTextView = rootView.findViewById(R.id.detailedMovieNameTextView);
        movieRatingTextView = rootView.findViewById(R.id.detailedMovieRatingTextView);
        movieReleaseDateTextView = rootView.findViewById(R.id.detailedMovieYearTextView);
        movieDescriptionTextView = rootView.findViewById(R.id.detailedMovieDescriptionTextView);
        orderMovieButton = rootView.findViewById(R.id.detailedMovieOrderOrReturnMovieButton);
        favoriteMovieButton = rootView.findViewById(R.id.deatiledMovieFavoriteFloatingButton);

        // Create or find the rental status TextView
        rentalStatusTextView = rootView.findViewById(R.id.movieDateTextView);

        // Set up click listeners
        favoriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDetailPresenter.onFavoriteButtonClick();
            }
        });

        orderMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDetailPresenter.onOrderButtonClick();
            }
        });

        // Initialize presenter
        movieDetailPresenter = new MovieDetailPresenter(this, getContext(), movie);

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

        return rootView;
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFavoriteButton(Boolean isFilled) {
        favoriteMovieButton.setImageResource(isFilled ? R.drawable.heart_filled : R.drawable.heart_unfilled);
    }

    @Override
    public void showMovieIsRented(Boolean isRentedByUser) {
        if (rentalStatusTextView != null) {
            rentalStatusTextView.setVisibility(View.VISIBLE);
            if (isRentedByUser) {
                rentalStatusTextView.setText(R.string.the_movie_is_rented_by_user);
            } else {
                rentalStatusTextView.setText(R.string.the_movie_is_already_rented);

            }
        }
    }

    @Override
    public void updateOrderButtonState(boolean isRenting) {
        if (orderMovieButton != null) {
            orderMovieButton.setEnabled(!isRenting);
            if (isRenting) {
                orderMovieButton.setText(R.string.ordering);
            } else {
                orderMovieButton.setText(R.string.order_movie);
            }
        }
    }

    @Override
    public void setOrderButtonText(String buttonText) {
        orderMovieButton.setText(buttonText);
    }

    @Override
    public void showReturnText(Boolean show) {
        rentalStatusTextView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}