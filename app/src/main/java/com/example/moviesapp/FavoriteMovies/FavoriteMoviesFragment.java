package com.example.moviesapp.FavoriteMovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.Adapters.FavoriteMoviesAdapter;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.R;

import java.util.ArrayList;

/**
 * Fragment to display user's favorite movies
 */
public class FavoriteMoviesFragment extends Fragment implements FavoriteMoviesContract.View {

    // Keep a reference to UI components
    private RecyclerView recyclerView;
    private FavoriteMoviesAdapter adapter;
    private View rootView;
    private FavoriteMoviesPresenter favoriteMoviesPresenter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteMoviesFragment newInstance(String param1, String param2) {
        FavoriteMoviesFragment fragment = new FavoriteMoviesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorite_movies, container, false);

        // Find RecyclerView
        recyclerView = rootView.findViewById(R.id.favoriteMoviesRecycler);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with click listener for movie items
        adapter = new FavoriteMoviesAdapter(getContext(), new ArrayList<>(), new FavoriteMoviesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                favoriteMoviesPresenter.onMovieClick(movie);
            }
        });
        recyclerView.setAdapter(adapter);

        // Create presenter and load data
        favoriteMoviesPresenter = new FavoriteMoviesPresenter(this, getContext());

        // Show loading initially
        showProgress();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        favoriteMoviesPresenter.refreshFavorites();
    }

    @Override
    public void showProgress() {
        // No progress indicator in this layout
    }

    @Override
    public void hideProgress() {
        // No progress indicator in this layout
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initializeRecyclerView(ArrayList<Movie> movies) {
        adapter = new FavoriteMoviesAdapter(getContext(), movies, new FavoriteMoviesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                favoriteMoviesPresenter.onMovieClick(movie);
            }
        });
        recyclerView.setAdapter(adapter);

        // If you want to add an empty state message, you could add a TextView to your layout
        // and toggle its visibility here
    }

    @Override
    public void updateRecyclerView(ArrayList<Movie> movies) {
        adapter.updateMovies(movies);
    }

    @Override
    public void navigateToMovieDetails(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        Navigation.findNavController(rootView).navigate(
                R.id.action_favoriteMoviesFragment_to_movieDetailFragment,
                bundle
        );
    }
}