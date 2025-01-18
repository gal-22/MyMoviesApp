package com.example.moviesapp.FindMovies;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.moviesapp.Login.LoginContract;
import com.example.moviesapp.Login.LoginModel;

import org.json.JSONObject;

public class FindMoviesPresenter implements FindMoviesContract.Presenter{
    private FindMoviesContract.View findMoviesView;
    private FindMoviesContract.Model findMoviesModel;

    public FindMoviesPresenter(FindMoviesContract.View findMoviesView, Context context) {
        this.findMoviesView = findMoviesView;
        this.findMoviesModel = new FindMoviesModel(this, context);
    }
    @Override
    public void onSearchViewTextChanged(String text) {

    }

    @Override
    public void onMoviesLoaded(@Nullable Exception e, @Nullable JSONObject response) {

    }
}
