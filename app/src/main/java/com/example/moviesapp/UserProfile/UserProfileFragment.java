package com.example.moviesapp.UserProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.moviesapp.R;
import com.example.moviesapp.SessionManager;

public class UserProfileFragment extends Fragment implements UserProfileContract.View {

    // Views
    private View rootView;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView rentedCountTextView;
    private TextView favoriteCountTextView;
    private Button logoutButton;
    private ProgressBar progressBar;

    private SessionManager sessionManager;


    // Presenter
    private UserProfilePresenter presenter;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        if (getContext() != null) {
            sessionManager = new SessionManager(getContext());
        }
        // Initialize views
        userNameTextView = rootView.findViewById(R.id.textView4);
        userEmailTextView = rootView.findViewById(R.id.textView5);
        logoutButton = rootView.findViewById(R.id.button);
        progressBar = rootView.findViewById(R.id.userProfileProgressBar);

        // Set up logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogoutClick();
            }
        });

        // Create presenter
        presenter = new UserProfilePresenter(this, getContext());

        // Load user profile
        presenter.loadUserProfile();

        return rootView;
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
    public void setUserName(String name) {
        if (userNameTextView != null) {
            userNameTextView.setText(name);
        }
    }

    @Override
    public void setUserEmail(String email) {
        if (userEmailTextView != null) {
            userEmailTextView.setText(email);
        }
    }

    @Override
    public void setMoviesRentedCount(int count) {
        if (rentedCountTextView != null) {
            rentedCountTextView.setText(String.valueOf(count));
        }
    }

    @Override
    public void setFavoriteMoviesCount(int count) {
        if (favoriteCountTextView != null) {
            favoriteCountTextView.setText(String.valueOf(count));
        }
    }

    @Override
    public void navigateToLogin() {
        // Create NavOptions to pop the entire back stack up to the login fragment
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true) // Pop up to login, inclusive
                .build();
        sessionManager.clearSession();
        // Navigate to login fragment with the nav options
        Navigation.findNavController(rootView).navigate(
                R.id.loginFragment,  // Direct destination instead of using an action
                null,                // No arguments needed
                navOptions           // Options to clear back stack
        );

    }
}