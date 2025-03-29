package com.example.moviesapp.UserProfile;

import android.content.Context;

public class UserProfilePresenter implements UserProfileContract.Presenter {
    private UserProfileContract.View userProfileView;
    private UserProfileContract.Model userProfileModel;
    private Context context;

    public UserProfilePresenter(UserProfileContract.View userProfileView, Context context) {
        this.userProfileView = userProfileView;
        this.context = context;
        this.userProfileModel = new UserProfileModel(this, context);
    }

    @Override
    public void loadUserProfile() {
        userProfileView.showProgress();
        userProfileModel.getUserProfile();
    }

    @Override
    public void onLogoutClick() {
        userProfileView.showProgress();
        userProfileModel.logout();
        userProfileView.hideProgress();
        userProfileView.navigateToLogin();
    }

    @Override
    public void onProfileLoaded(String name, String email) {
        userProfileView.hideProgress();
        userProfileView.setUserName(name);
        userProfileView.setUserEmail(email);
    }

    @Override
    public void onError(String errorMessage) {
        userProfileView.hideProgress();
        userProfileView.showError(errorMessage);
    }
}