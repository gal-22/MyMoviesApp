package com.example.moviesapp.FindMovies;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.moviesapp.Adapters.MoviesRecyclerViewAdapter;
import com.example.moviesapp.ProjectClasses.Movie;
import com.example.moviesapp.ProjectClasses.MovieGenre;
import com.example.moviesapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindMoviesFragment extends Fragment implements FindMoviesContract.View{

    // Keep a reference to the RecyclerView and Adapter
    private RecyclerView recyclerView;
    private MoviesRecyclerViewAdapter adapter;
    private MoviesRecyclerViewAdapter.OnMovieClickListener listener;
    private ArrayList<Movie> movies;

    private FindMoviesPresenter findMoviesPresenter;

    View rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindMoviesFragment newInstance(String param1, String param2) {
        FindMoviesFragment fragment = new FindMoviesFragment();
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
        this.rootView = inflater.inflate(R.layout.fragment_find_movies, container, false);
        this.listener = new MoviesRecyclerViewAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                FindMoviesFragment.this.findMoviesPresenter.onMovieClick(movie);
            }
        };
        SwitchCompat switchAction = rootView.findViewById(R.id.findMoviesActionSwitch);
        switchAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    findMoviesPresenter.onGenreStatusChanged(MovieGenre.ACTION.getId(), isChecked);
            }
        });

        SwitchCompat switchComedy = rootView.findViewById(R.id.findMoviesComedySwitch);
        switchComedy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.COMEDY.getId(), isChecked);
            }
        });

        SwitchCompat switchDrama = rootView.findViewById(R.id.findMoviesDramaSwitch);
        switchDrama.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.DRAMA.getId(), isChecked);
            }
        });

        SwitchCompat switchAnimation = rootView.findViewById(R.id.findMoviesAnimationSwitch);
        switchAnimation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.ANIMATION.getId(), isChecked);
            }
        });

        SwitchCompat switchAdventure = rootView.findViewById(R.id.findMoviesAdventureSwitch);
        switchAdventure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.ADVENTURE.getId(), isChecked);
            }
        });

        SwitchCompat switchCrime = rootView.findViewById(R.id.findMoviesCrimeSwitch);
        switchCrime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.CRIME.getId(), isChecked);
            }
        });

        SwitchCompat switchDocumentary = rootView.findViewById(R.id.findMoviesDocumentarySwitch);
        switchDocumentary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.DOCUMENTARY.getId(), isChecked);
            }
        });

        SwitchCompat switchFamily = rootView.findViewById(R.id.findMoviesFamilySwitch);
        switchFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findMoviesPresenter.onGenreStatusChanged(MovieGenre.FAMILY.getId(), isChecked);
            }
        });
        recyclerView = rootView.findViewById(R.id.findMoviesRecyclerView);
        this.findMoviesPresenter = new FindMoviesPresenter(FindMoviesFragment.this, getContext());
        // Inflate the layout for this fragment
        return this.rootView;
    }

    @Override
    public void showProgress() {
        // Implement this method in the future if needed.
    }

    @Override
    public void hideProgress() {
        // Implement this method in the future if needed.
    }

    @Override
    public void showError(String message) {
        // Implement this method in the future if needed.
    }

    @Override
    public void initializeRecyclerView(ArrayList<Movie> movies) {
        adapter = new MoviesRecyclerViewAdapter(this.rootView.getContext(), movies, listener);
        this.movies = movies;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.rootView.getContext()));
        recyclerView.setAdapter(adapter);
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
                R.id.action_findMoviesFragment_to_movieDetailFragment,
                bundle// Implement this method in the future if needed.
        );
    }
}