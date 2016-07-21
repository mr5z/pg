package com.nkraft.pogo.controller.initial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.nkraft.pogo.R;
import com.nkraft.pogo.utils.Debug;

/**
 * Created by mark on 20/07/2016.
 */
public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int REQUEST_SIGN_IN = 1;

    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                        .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Debug.log("Sign-in result: %b", result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Debug.log("Email: %s, Display name: %s, Photo URL: %s", account.getEmail(), account.getDisplayName(), account.getPhotoUrl());
        } else {

        }
    }

}
