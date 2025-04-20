package com.example.moviesapp.MovieDetails;

public interface MovieDetailContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showMessage(String message);
        void setFavoriteButton(Boolean isFilled);

        // New methods for rental functionality
        void showMovieIsRented(Boolean isRentedByUser);
        void updateOrderButtonState(boolean isRenting);

        void setOrderButtonText(String orderMovie);

        void showOrderButton(boolean show);
        void showReturnText(Boolean show);
    }

    interface Presenter {
        void onFavoriteButtonClick();
        void onFinishedChangingFavoriteStatus(Boolean isFavorite);
        void onGetFavoriteStatus(Boolean isFavorite);
        void onError(String s);

        // New methods for rental functionality
        void onOrderButtonClick();
        void onRentalStatusLoaded(boolean isRented);
        void onRentalCompleted(boolean success, String message);

        void onReturnCompleted(boolean success, String message);
    }

    interface Model {
        void getFavoriteStatus();
        void changeMovieFavoriteStatus();

        // New methods for rental functionality
        void getRentalStatus();
        void rentMovie();
        void returnMovie();
    }
}