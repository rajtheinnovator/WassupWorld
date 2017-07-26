package com.example.android.wassupworld.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


/**
 * Created by dell on 7/13/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}