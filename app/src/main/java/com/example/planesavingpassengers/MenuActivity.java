package com.example.planesavingpassengers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MenuActivity extends AppCompatActivity {

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
        startActivity(scoresIntent);
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