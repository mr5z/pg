package com.nkraft.pogo.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.nkraft.pogo.R;
import com.nkraft.pogo.controller.initial.SignInFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "com.nkraft.pogo:fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean signedIn = false;
        if (!signedIn) {
            showSignIn();
        }
    }

    private void showSignIn() {
        Fragment fragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

}
