package com.example.moviesapp.MovieDetails;

import android.content.Context;
import android.util.Log;

import com.example.moviesapp.ProjectClasses.Movie;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private static final String TAG = "MovieDetailPresenter";
    private Movie movie;
    private MovieDetailContract.View movieDetailView;
    private MovieDetailContract.Model movieDetailModel;
    private Context context;

    public MovieDetailPresenter(MovieDetailContract.View movieDetailView, Context context, Movie movie) {
        this.movieDetailView = movieDetailView;
        this.movieDetailModel = new MovieDetailModel(context, movie, this);
        this.context = context;
        this.movie = movie;

        // Check favorite status
        movieDetailModel.getFavoriteStatus();

        // Check rental status
        checkRentalStatus();
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
    public void checkRentalStatus() {
        movieDetailView.showProgress();
        movieDetailModel.getRentalStatus();
    }

    @Override
    public void onRentalStatusLoaded(boolean isRented, String returnDate) {
        movieDetailView.hideProgress();

        if (isRented) {
            // If the movie is currently rented, show the return date
            movieDetailView.showRentedUntil(returnDate);
        } else {
            // If the movie is not rented, show the order button
            movieDetailView.showOrderButton();
        }
    }

    @Override
    public void onOrderButtonClick() {
        // User clicked the order button, start rental process
        movieDetailView.showProgress();
        movieDetailView.updateOrderButtonState(true); // Disable button while processing
        movieDetailModel.rentMovie();
    }

    @Override
    public void onRentalCompleted(boolean success, String message) {
        movieDetailView.hideProgress();
        movieDetailView.updateOrderButtonState(false); // Re-enable button
        movieDetailView.showMessage(message);

        // If rental was successful, update UI
        if (success) {
            // The rental status will be updated by a separate call to getRentalStatus()
            // which is triggered in the model after successful rental
        }
    }
}