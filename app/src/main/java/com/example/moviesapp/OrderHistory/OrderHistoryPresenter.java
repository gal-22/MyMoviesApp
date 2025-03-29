package com.example.moviesapp.OrderHistory;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.moviesapp.Parsers.MovieParser;
import com.example.moviesapp.ProjectClasses.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryPresenter implements OrderHistoryContract.Presenter {
    private OrderHistoryContract.View orderHistoryView;
    private OrderHistoryContract.Model orderHistoryModel;
    private ArrayList<Movie> movies;

    public OrderHistoryPresenter(OrderHistoryContract.View orderHistoryView, Context context) {
        this.orderHistoryView = orderHistoryView;
        this.orderHistoryModel = new OrderHistoryModel(this, context);
        this.orderHistoryModel.loadOrderHistory();
    }

    @Override
    public void onOrderHistoryLoaded(@Nullable Exception e, @Nullable JSONObject response) throws JSONException {
        if (response != null) {
            JSONArray arr = response.optJSONArray("rentals");
            ArrayList<Movie> movies = MovieParser.parseOrderHistoryMovies(arr != null ? arr : new JSONArray());
            this.movies = movies;
            orderHistoryView.initializeRecyclerView(movies);
        }
    }

    @Override
    public void refreshOrderHistory() {
        orderHistoryView.showProgress();
        orderHistoryModel.loadOrderHistory();
    }

    @Override
    public void onMovieClick(Movie movie) {
        orderHistoryView.navigateToMovieDetails(movie);
    }
}