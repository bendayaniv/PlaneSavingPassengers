package com.example.planesavingpassengers.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.planesavingpassengers.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MenuActivity extends AppCompatActivity {

    public static final String INDICATION = "INDICATION";
//    public static final String IS_ON = "IS_ON";

    private MaterialTextView menu_LBL_headline;
    private MaterialButton menu_LBL_startGame;
    private MaterialButton menu_LBL_scores;

    private Switch menu_SWITCH_btn;
    private Switch menu_SWITCH_speed;

    // 1000 is default because the slow option is the default option
    private int DELAY = 1000;
    // Button option is the default option
    private boolean buttonsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

//        // Create access to device location
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        }

//        while (ActivityCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            Toast.makeText(this, "We need your location...", Toast.LENGTH_LONG).show();
//        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        findViews();

        menu_LBL_headline.setText("Save the Passengers!");
//        menu_SWITCH_speed.setOnCheckedChangeListener((compoundButton, b) -> {
//            Toast.makeText(this, "menu_SWITCH_speed", Toast.LENGTH_SHORT).show();
//        });
//        menu_SWITCH_btn.setOnCheckedChangeListener((compoundButton, b) -> {
//            Toast.makeText(this, "menu_SWITCH_btn", Toast.LENGTH_SHORT).show();
//        });
        menuButtons();
    }

//    private boolean isGPSEnabled() {
//        LocationManager locationManager = null;
//        boolean gpsEnabled = false;
//        if (locationManager == null)
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        try {
//        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////        } catch (Exception ex) {
//
////        }
//        return gpsEnabled;
//    }


    private void findViews() {
        menu_LBL_headline = findViewById(R.id.menu_LBL_headline);
        menu_LBL_startGame = findViewById(R.id.menu_Btn_startGame);
        menu_LBL_scores = findViewById(R.id.menu_LBL_scores);
        menu_SWITCH_btn = findViewById(R.id.menu_SWITCH_btn);
        menu_SWITCH_speed = findViewById(R.id.menu_SWITCH_speed);
    }

    private void menuButtons() {
        menu_LBL_startGame.setOnClickListener(v -> openGameActivity());
        menu_LBL_scores.setOnClickListener(v -> openScoresActivity());
    }

    private void openScoresActivity() {
        Intent scoresIntent = new Intent(this, ScoresActivity.class);
        scoresIntent.putExtra(ScoresActivity.INDICATION, false);
        startActivity(scoresIntent);
//        Intent scoresIntent = new Intent(this, GPSTryActivity.class);
//        startActivity(scoresIntent);
//        Intent scoreListIntent = new Intent(this, ListFragment.class);
//        startActivity(scoreListIntent);
        finish();
    }

    private void openGameActivity() {
        Intent gameIntent = new Intent(this, GameActivity.class);
        setDetails();
        gameIntent.putExtra(GameActivity.KEY_DELAY, DELAY);
        gameIntent.putExtra(GameActivity.KEY_BUTTONS, buttonsEnabled);
        startActivity(gameIntent);
        finish();
    }

    private void setDetails() {
        if (menu_SWITCH_speed.isChecked() == true) {
            DELAY = 500;
        } else {
            DELAY = 1000;
        }

        if (menu_SWITCH_btn.isChecked() == true) {
            buttonsEnabled = false;
        } else {
            buttonsEnabled = true;
        }
    }
}