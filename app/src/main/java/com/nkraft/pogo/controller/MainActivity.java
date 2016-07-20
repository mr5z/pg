package com.nkraft.pogo.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.nkraft.pogo.R;
import com.nkraft.pogo.controller.initial.SignInActivity;
import com.nkraft.pogo.utils.Debug;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean signedIn = false;
        if (!signedIn) {
            showSignIn();
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
    }

    private void showSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
    }

}
