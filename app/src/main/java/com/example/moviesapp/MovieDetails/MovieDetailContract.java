package com.example.moviesapp.MovieDetails;

import androidx.annotation.Nullable;
import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public interface MovieDetailContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showMessage(String message);
        void setFavoriteButton(Boolean isFilled);

        // New methods for rental functionality
        void showOrderButton();
        void showRentedUntil(String returnDate);
        void updateOrderButtonState(boolean isRenting);
    }

    interface Presenter {
        void onFavoriteButtonClick();
        void onFinishedChangingFavoriteStatus(Boolean isFavorite);
        void onGetFavoriteStatus(Boolean isFavorite);
        void onError(String s);

        // New methods for rental functionality
        void onOrderButtonClick();
        void checkRentalStatus();
        void onRentalStatusLoaded(boolean isRented, @Nullable String returnDate);
        void onRentalCompleted(boolean success, String message);
    }

    interface Model {
        void getFavoriteStatus();
        void changeMovieFavoriteStatus();

        // New methods for rental functionality
        void getRentalStatus();
        void rentMovie();
    }
}