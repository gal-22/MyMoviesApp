package com.example.moviesapp.MovieDetails;

import android.content.Context;
import android.util.Log;

import com.example.moviesapp.ProjectClasses.Movie;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private static final String TAG = "MovieDetailPresenter";
    private Movie movie;
    private MovieDetailContract.View movieDetailView;
    private MovieDetailContract.Model movieDetailModel;

    public MovieDetailPresenter(MovieDetailContract.View movieDetailView, Context context, Movie movie) {
        this.movieDetailView = movieDetailView;
        this.movieDetailModel = new MovieDetailModel(context, movie, this);
        this.movie = movie;
        movieDetailView.setFavoriteButton(movie.isFavorite());
        if (movie.isRented()) {
            movieDetailView.showMovieIsRented(movie.isRentedByUser());
            movieDetailView.setOrderButtonText("Return Movie");
            movieDetailView.showReturnText(true);
        } else {
            movieDetailView.setOrderButtonText("Order Movie");
            movieDetailView.showReturnText(false);
        }
    }

    @Override
    public void onFavoriteButtonClick() {
        movieDetailModel.changeMovieFavoriteStatus();
    }

    @Override
    public void onFinishedChangingFavoriteStatus(Boolean isFavorite) {
        movieDetailView.setFavoriteButton(isFavorite);
    }

    @Override
    public void onGetFavoriteStatus(Boolean isFavorite) {
        movieDetailView.setFavoriteButton(isFavorite);
    }

    @Override
    public void onError(String errorMessage) {
        Log.e(TAG, "Error: " + errorMessage);
        movieDetailView.showMessage(errorMessage);
    }

    // New rental-related methods



    @Override
    public void onRentalStatusLoaded(boolean isRented) {
        movieDetailView.hideProgress();

        if (isRented) {
            // If the movie is currently rented, show the return date
            movieDetailView.showMovieIsRented(movie.isRentedByUser());
            movieDetailView.setOrderButtonText("Return Movie");

        } else {
            // If the movie is not rented, show the order button
            movieDetailView.setOrderButtonText("Order Movie");
        }
    }

    @Override
    public void onOrderButtonClick() {
        // User clicked the order button, start rental process
        movieDetailView.showProgress();
        movieDetailView.updateOrderButtonState(true); // Disable button while processing
        if (movie.isRented()) {
            movieDetailModel.returnMovie();
        } else {
            movieDetailModel.rentMovie();
        }
    }

    @Override
    public void onRentalCompleted(boolean success, String message) {
        movieDetailView.hideProgress();
        movieDetailView.updateOrderButtonState(false); // Re-enable button
        movieDetailView.showMessage(message);
        // If rental was successful, update UI
        if (success) {
            this.movie.setRentedByUser(true);
            this.movie.setRented(true);
            movieDetailView.showReturnText(true);
            movieDetailView.showMovieIsRented(true);
            movieDetailView.setOrderButtonText("Return Movie");
        }
    }

    @Override
    public void onReturnCompleted(boolean success, String message) {
        movieDetailView.hideProgress();
        movieDetailView.updateOrderButtonState(false); // Re-enable button
        movieDetailView.showMessage(message);

        // If return was successful, update UI
        if (success) {
            this.movie.setRentedByUser(false);
            this.movie.setRented(false);
            movieDetailView.showReturnText(false);
            movieDetailView.setOrderButtonText("Order Movie");
        }
    }
}