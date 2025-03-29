package com.example.moviesapp.UserProfile;

public interface UserProfileContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showError(String message);
        void setUserName(String name);
        void setUserEmail(String email);
        void setMoviesRentedCount(int count);
        void setFavoriteMoviesCount(int count);
        void navigateToLogin();
    }

    interface Presenter {
        void loadUserProfile();
        void onLogoutClick();
        void onProfileLoaded(String name, String email);
        void onError(String errorMessage);
    }

    interface Model {
        void getUserProfile();
        void logout();
    }
}