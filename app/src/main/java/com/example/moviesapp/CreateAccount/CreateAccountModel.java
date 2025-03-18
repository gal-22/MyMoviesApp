package com.example.moviesapp.CreateAccount;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model class for handling API communication for user registration
 */
public class CreateAccountModel implements CreateAccountContract.Model {

    private static final String TAG = "CreateAccountModel";
    private CreateAccountContract.Presenter createAccountPresenter;

    public CreateAccountModel(CreateAccountContract.Presenter presenter) {
        this.createAccountPresenter = presenter;
    }

    @Override
    public void registerUser(String name, String email, String password, Context context) {
        // Use 10.0.2.2 for localhost when running on an Android emulator
        String url = "http://10.0.2.2:8080/api/auth/register";

        // Build the JSON request body according to server expectations
        JSONObject jsonBody = new JSONObject();
        try {
            // Match the server's User class structure
            jsonBody.put("username", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            createAccountPresenter.onSignUpLoadCompleted(e, null);
            return;
        }

        Log.d(TAG, "Register request body: " + jsonBody.toString());

        // Create the POST request using Volley
        JsonObjectRequest registerRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Registration success: " + response.toString());
                        createAccountPresenter.onSignUpLoadCompleted(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration error", error);

                        // Check if this might be a duplicate email error
                        if (error.networkResponse != null &&
                                error.networkResponse.statusCode == 500) {

                            // Extract raw response
                            String rawResponse = new String(error.networkResponse.data);

                            if (rawResponse.contains("Duplicate entry") &&
                                    rawResponse.contains("users.email")) {

                                // Create a custom error object to provide a better message
                                JSONObject errorObj = new JSONObject();
                                try {
                                    errorObj.put("message", "This email is already registered. Please use a different email or try logging in.");
                                    createAccountPresenter.onSignUpLoadCompleted(null, errorObj);
                                    return;
                                } catch (JSONException e) {
                                    // Fall through to default error handling
                                }
                            }
                        }

                        // Default error handling
                        createAccountPresenter.onSignUpLoadCompleted(error, null);
                    }
                }
        );

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(registerRequest);
    }
}