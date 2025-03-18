package com.example.moviesapp.CreateAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moviesapp.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment for user registration screen
 */
public class CreateAccountFragment extends Fragment implements CreateAccountContract.View {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private TextView loginLinkTextView;

    private CreateAccountPresenter presenter;
    private View rootView;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_account, container, false);

        // Initialize UI components
        nameEditText = rootView.findViewById(R.id.editTextText);
        emailEditText = rootView.findViewById(R.id.editTextText2);
        passwordEditText = rootView.findViewById(R.id.editTextTextPassword2);

        // Add the create account button and progress bar
        createAccountButton = rootView.findViewById(R.id.createAccountButton);
        progressBar = rootView.findViewById(R.id.createAccountProgressBar);

        // Initialize presenter
        presenter = new CreateAccountPresenter(this, requireContext());

        // Set up button click listener
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                presenter.onCreateAccountButtonClicked(name, email, password);
            }
        });

        // Set up login link if user already has an account
        if (loginLinkTextView != null) {
            loginLinkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToLogin();
                }
            });
        }

        return rootView;
    }

    @Override
    public void showProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (createAccountButton != null) {
            createAccountButton.setEnabled(false);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (createAccountButton != null) {
            createAccountButton.setEnabled(true);
        }
    }

    @Override
    public void showError(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToLogin() {
        // Navigate to login screen using the Navigation Component
        Navigation.findNavController(rootView).navigate(R.id.action_createAccountFragment_to_homePageFragment);
    }

    @Override
    public void showNameError(String error) {
        nameEditText.setError(error);
    }

    @Override
    public void showEmailError(String error) {
        emailEditText.setError(error);
    }

    @Override
    public void showPasswordError(String error) {
        passwordEditText.setError(error);
    }
}