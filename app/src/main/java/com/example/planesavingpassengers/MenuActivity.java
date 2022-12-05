package com.example.planesavingpassengers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MenuActivity extends AppCompatActivity {

    private MaterialTextView menu_LBL_headline;
    private MaterialButton menu_LBL_startGame;
    private MaterialButton menu_LBL_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();

        menu_LBL_headline.setText("Save the Passengers!");

        menuButtons();
    }


    private void findViews() {
        menu_LBL_headline = findViewById(R.id.menu_LBL_headline);
        menu_LBL_startGame = findViewById(R.id.menu_Btn_startGame);
        menu_LBL_scores = findViewById(R.id.menu_LBL_scores);
    }

    private void menuButtons() {
        menu_LBL_startGame.setOnClickListener(v -> openGameActivity());
        menu_LBL_scores.setOnClickListener(v -> openScoreAndMapActivity());
    }

    private void openScoreAndMapActivity() {
        Toast.makeText(this, "Birds attacked you", Toast.LENGTH_LONG)
                .show();
    }

    private void openGameActivity() {
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
        finish();
    }
}