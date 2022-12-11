package com.example.planesavingpassengers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.planesavingpassengers.interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.views.ListFragment;
import com.example.planesavingpassengers.views.MapFragment;
import com.google.android.material.button.MaterialButton;

public class ScoresActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private MaterialButton scores_BTN_backToMenu;

    Callback_userProtocol callback_userProtocol = new Callback_userProtocol()  {
        @Override
        public void sendLocation(double latitude, double longitude) {
            showUserLocation(latitude, longitude);
        }
    };

    private void showUserLocation(double latitude, double longitude) {
//        double latitude = 30.9999;
//        double longitude = 32.6799;
        mapFragment.zoom(latitude, longitude);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        scores_BTN_backToMenu = findViewById(R.id.scores_BTN_backToMenu);
        scores_BTN_backToMenu.setOnClickListener(v -> backToMenu());

        listFragment = new ListFragment();
        listFragment.setCallback(callback_userProtocol);

        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_map, mapFragment).commit();
    }

    private void backToMenu() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }
}