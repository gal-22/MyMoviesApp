package com.example.moviesapp.MovieDetails;


import android.content.Context;

import com.example.moviesapp.FindMovies.FindMoviesContract;
import com.example.moviesapp.FindMovies.FindMoviesModel;
import com.example.moviesapp.ProjectClasses.Movie;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private Movie movie;
    private MovieDetailContract.View movieDetailView;
    private MovieDetailContract.Model movieDetailModel;
    private Context context;
    public MovieDetailPresenter(MovieDetailContract.View movieDetailView, Context context, Movie movie) {
        this.movieDetailView = movieDetailView;
        this.movieDetailModel = new MovieDetailModel(context, movie, this);
        this.context = context;
        this.movie = movie;
        movieDetailModel.getFavoriteStatus();
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
    public void onError(String s) {

    }
}
