package com.nkraft.pg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nkraft.pg.utils.Debug;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debug.log("hey there: %s", "mark");
    }
}
