package com.example.moviesapp.MovieDetails;

import androidx.annotation.Nullable;

import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public interface MovieDetailContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showMessage(String message);
        void setFavoriteButton(Boolean isFilled);


    }

    interface Presenter {
        void onFavoriteButtonClick();

        void onFinishedChangingFavoriteStatus(Boolean isFavorite);

        void onGetFavoriteStatus(Boolean isFavorite);

        void onError(String s);
    }

    interface Model {
        void getFavoriteStatus();
        void changeMovieFavoriteStatus();
    }
}
