package com.example.planesavingpassengers;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private final int DELAY = /*1000*/500;
    private final int Y_LENGTH = 12;
    private final int X_LENGTH = 5;
    private final int BOARD_LIMIT = 10;
    private final int DEFAULT_X_FOR_PLANE = X_LENGTH / 2;
    private final int STEP_RIGHT_OF_PLANE = 1;
    private final int STEP_LEFT_OF_PLANE = -1;
    private final String LEFT_DIRECTION = "LEFT";
    private final String RIGHT_DIRECTION = "RIGHT";

    private ExtendedFloatingActionButton gameActivity_FAB_left;
    private ExtendedFloatingActionButton gameActivity_FAB_right;
    private ShapeableImageView[][] gameBoard;
    private ShapeableImageView[] game_IMG_hearts;

    private ManagerActivity gameManager;
    private Timer timer;
    long startTime = 0;

//    public final MediaPlayer crowd_panic = MediaPlayer.create(this, R.raw.crowd_panic);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        findViews();
        createMovingPlaneButtons();
        gameManager = new ManagerActivity(game_IMG_hearts.length, Y_LENGTH, X_LENGTH, DEFAULT_X_FOR_PLANE, BOARD_LIMIT);
    }

    private void findViews() {
        gameActivity_FAB_left = findViewById(R.id.gameActivity_FAB_left);
        gameActivity_FAB_right = findViewById(R.id.gameActivity_FAB_right);
        gameBoard = new ShapeableImageView[][]{{findViewById(R.id.game_IMG_0_0), findViewById(R.id.game_IMG_0_1), findViewById(R.id.game_IMG_0_2), findViewById(R.id.game_IMG_0_3), findViewById(R.id.game_IMG_0_4)},
                {findViewById(R.id.game_IMG_1_0), findViewById(R.id.game_IMG_1_1), findViewById(R.id.game_IMG_1_2), findViewById(R.id.game_IMG_1_3), findViewById(R.id.game_IMG_1_4)},
                {findViewById(R.id.game_IMG_2_0), findViewById(R.id.game_IMG_2_1), findViewById(R.id.game_IMG_2_2), findViewById(R.id.game_IMG_2_3), findViewById(R.id.game_IMG_2_4)},
                {findViewById(R.id.game_IMG_3_0), findViewById(R.id.game_IMG_3_1), findViewById(R.id.game_IMG_3_2), findViewById(R.id.game_IMG_3_3), findViewById(R.id.game_IMG_3_4)},
                {findViewById(R.id.game_IMG_4_0), findViewById(R.id.game_IMG_4_1), findViewById(R.id.game_IMG_4_2), findViewById(R.id.game_IMG_4_3), findViewById(R.id.game_IMG_4_4)},
                {findViewById(R.id.game_IMG_5_0), findViewById(R.id.game_IMG_5_1), findViewById(R.id.game_IMG_5_2), findViewById(R.id.game_IMG_5_3), findViewById(R.id.game_IMG_5_4)},
                {findViewById(R.id.game_IMG_6_0), findViewById(R.id.game_IMG_6_1), findViewById(R.id.game_IMG_6_2), findViewById(R.id.game_IMG_6_3), findViewById(R.id.game_IMG_6_4)},
                {findViewById(R.id.game_IMG_7_0), findViewById(R.id.game_IMG_7_1), findViewById(R.id.game_IMG_7_2), findViewById(R.id.game_IMG_7_3), findViewById(R.id.game_IMG_7_4)},
                {findViewById(R.id.game_IMG_8_0), findViewById(R.id.game_IMG_8_1), findViewById(R.id.game_IMG_8_2), findViewById(R.id.game_IMG_8_3), findViewById(R.id.game_IMG_8_4)},
                {findViewById(R.id.game_IMG_9_0), findViewById(R.id.game_IMG_9_1), findViewById(R.id.game_IMG_9_2), findViewById(R.id.game_IMG_9_3), findViewById(R.id.game_IMG_9_4)},
                {findViewById(R.id.game_IMG_10_0), findViewById(R.id.game_IMG_10_1), findViewById(R.id.game_IMG_10_2), findViewById(R.id.game_IMG_10_3), findViewById(R.id.game_IMG_10_4)},
                {findViewById(R.id.game_IMG_11_0), findViewById(R.id.game_IMG_11_1), findViewById(R.id.game_IMG_11_2), findViewById(R.id.game_IMG_11_3), findViewById(R.id.game_IMG_11_4)}};

        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
    }

    private void createMovingPlaneButtons() {
        gameActivity_FAB_right.setOnClickListener(v -> movePlane(RIGHT_DIRECTION));
        gameActivity_FAB_left.setOnClickListener(v -> movePlane(LEFT_DIRECTION));
    }

    private void loadImage(int imageNum, ShapeableImageView imageView) {
        Glide.with(this).load(imageNum).fitCenter().into(imageView);
    }

    private void deleteImage(ShapeableImageView imageView) {
        Glide.with(this).clear(imageView);
    }

    /**
     * First - we delete the images of the plane from his current location
     * Second - we check if the plane goes right or left and move him
     * Then - we load the plane image to his new location
     *
     * @param direction = the direction of the plane
     */
    private void movePlane(String direction) {
        deleteImage(gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
        if (direction.equals(RIGHT_DIRECTION))
            gameManager.movePlane(STEP_RIGHT_OF_PLANE);
        else if (direction.equals(LEFT_DIRECTION))
            gameManager.movePlane(STEP_LEFT_OF_PLANE);
        loadImage(gameManager.getPlane().getObjectImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
    }

    /**
     * Clean the game board
     */
    private void cleanTheBoard() {
        for (int i = Y_LENGTH - 1; i > 0; i--) {
            for (int j = 0; j < X_LENGTH; j++) {
                deleteImage(gameBoard[i][j]);
            }
        }
    }

    /**
     * Before moving all the existing objects on the board (except the plane!), we must clean the board of images
     * Then move all the objects
     * Then load all the images of the objects in their new locations
     * And finally load the image of the plane in his location
     */
    private void moveAllObjects() {
        cleanTheBoard();
        gameManager.moveObjectsDown(BOARD_LIMIT, X_LENGTH);
        loadAllObjects();
        loadImage(gameManager.getPlane().getObjectImage(), gameBoard[BOARD_LIMIT][gameManager.getPlane().getX()]);
    }

    private void loadAllObjects() {
        Object[][] board = gameManager.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //Checking if there is exist object in this location and if it reached to the limit he can go out of the board
                if (board[i][j] != null && board[i][j].getY() <= BOARD_LIMIT + 1) {
                    loadImage(board[i][j].getObjectImage(), gameBoard[board[i][j].getY()][board[i][j].getX()]);
                }
            }
        }
    }

    private void crashToast() {
        Toast.makeText(this, "Birds attacked you", Toast.LENGTH_LONG)
                .show();
    }

    private void vibrateAll() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void refreshUI() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        seconds = seconds % 60;

        //Checking if there is objects to move,
        //if it does - move them
        if (gameManager.getNumOfObjects() > 0) {
            moveAllObjects();
        }

        //Create new bird every 2 seconds
        if (seconds % 2 == 0) {
            gameManager.createNewBird(-1, X_LENGTH);
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!gameManager.checkIfCrash(BOARD_LIMIT)) {
                        refreshUI();
                    } else {
                        if (gameManager.getPlane().getNumOfCrash() != 0) {
                            game_IMG_hearts[game_IMG_hearts.length - gameManager.getPlane().getNumOfCrash()].setVisibility(View.INVISIBLE);
                        }
                        crashToast();
                        vibrateAll();

                        gameManager.clearAllObjects();

                        loadImage(gameManager.getPlane().getExplodeImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);

                        gameManager.getPlane().setY(BOARD_LIMIT);
                        gameManager.getPlane().setX(DEFAULT_X_FOR_PLANE);
                    }
                });
            }
        }, DELAY, DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }
}