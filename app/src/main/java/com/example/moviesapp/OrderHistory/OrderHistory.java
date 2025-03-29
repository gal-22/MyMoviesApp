package com.example.moviesapp.OrderHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.Adapters.MoviesRecyclerViewAdapter;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;

import java.util.ArrayList;

/**
 * Fragment for displaying order/rental history
 */
public class OrderHistory extends Fragment implements OrderHistoryContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View rootView;

    // UI components
    private RecyclerView recyclerView;
    private MoviesRecyclerViewAdapter adapter;
    private TextView emptyStateTextView;
    private ProgressBar progressBar;

    // Presenter
    private OrderHistoryPresenter presenter;

    // Click listener for movies
    private MoviesRecyclerViewAdapter.OnMovieClickListener movieClickListener;

    public OrderHistory() {
        // Required empty public constructor
    }

    public static OrderHistory newInstance(String param1, String param2) {
        OrderHistory fragment = new OrderHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialize click listener
        movieClickListener = new MoviesRecyclerViewAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                presenter.onMovieClick(movie);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_history, container, false);

        // Initialize UI components
        recyclerView = rootView.findViewById(R.id.ordersHistoryRecyclerView);

        // These components should be added to your layout XML
        // progressBar = rootView.findViewById(R.id.orderHistoryProgressBar);
        // emptyStateTextView = rootView.findViewById(R.id.orderHistoryEmptyTextView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize presenter
        presenter = new OrderHistoryPresenter(this, getContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        presenter.refreshOrderHistory();
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
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initializeRecyclerView(ArrayList<Movie> movies) {
        adapter = new MoviesRecyclerViewAdapter(getContext(), movies, movieClickListener);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Movie> movies) {
        if (adapter != null) {
            adapter.updateMovies(movies);
        } else {
            initializeRecyclerView(movies);
        }
    }

    @Override
    public void showEmptyState() {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(View.VISIBLE);
        }

        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideEmptyState() {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(View.GONE);
        }

        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateToMovieDetails(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        Navigation.findNavController(rootView).navigate(
                R.id.action_orderHistory_to_movieDetailFragment,
                bundle// Implement this method in the future if needed.
        );
    }
}