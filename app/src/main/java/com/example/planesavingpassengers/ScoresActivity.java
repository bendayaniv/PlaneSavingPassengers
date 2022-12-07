package com.example.planesavingpassengers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ScoresActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
    }
}