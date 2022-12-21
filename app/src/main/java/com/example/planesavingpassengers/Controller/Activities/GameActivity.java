package com.example.planesavingpassengers.Controller.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planesavingpassengers.Utils.BackgroundSoundService;
import com.example.planesavingpassengers.Models.GameManager;
import com.example.planesavingpassengers.Models.Objects.Object;
import com.example.planesavingpassengers.Utils.MovementDetector;
import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Interfaces.MovementCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String KEY_DELAY = "KEY_DELAY";
    public static final String KEY_BUTTONS = "KEY_BUTTONS";
    private int DELAY = 1000;
    private boolean buttonsEnabled = true;

    private final int Y_LENGTH = 12;
    private final int X_LENGTH = 5;
    private final int PLANE_LINE = 10;
    private final int DEFAULT_X_FOR_PLANE = X_LENGTH / 2;
    private final int STEP_RIGHT_OF_PLANE = 1;
    private final int STEP_LEFT_OF_PLANE = -1;
    private final String LEFT_DIRECTION = "LEFT";
    private final String RIGHT_DIRECTION = "RIGHT";
    private final String STAY_IN_PUT = "";
    private final boolean DO_NOT_MOVE_PLANE_IMAGE = false;
    private final boolean MOVE_PLANE_IMAGE = true;
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private ExtendedFloatingActionButton gameActivity_FAB_left;
    private ExtendedFloatingActionButton gameActivity_FAB_right;
    private ShapeableImageView[][] gameBoard;
    private ShapeableImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;

    private GameManager gameManager;
    private Timer checkingHitTimer;
    private Timer movingObjectsTimer;
    long startTime = 0;

    private Intent scoresIntent;
    private Intent BackgroundSoundIntent;

    private MovementDetector movementDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        playBackgroundSound();

        findViews();


        gameManager = new GameManager(game_IMG_hearts.length, Y_LENGTH, X_LENGTH, DEFAULT_X_FOR_PLANE, PLANE_LINE);

        // Get the details of the game from the previous activity
        Intent prevIntent = getIntent();
        DELAY = prevIntent.getIntExtra(KEY_DELAY, 1000);
        buttonsEnabled = prevIntent.getBooleanExtra(KEY_BUTTONS, true);

        if (buttonsEnabled == true) {
            createMovingPlaneButtons();
        } else {
            initStepDetector();
            gameActivity_FAB_left.setVisibility(View.INVISIBLE);
            gameActivity_FAB_right.setVisibility(View.INVISIBLE);
        }

        loadImage(gameManager.getPlane().getObjectImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
        startTime = System.currentTimeMillis();
    }

    /**
     * This methos plays the background sound
     */
    public void playBackgroundSound() {
        BackgroundSoundIntent = new Intent(GameActivity.this, BackgroundSoundService.class);
        startService(BackgroundSoundIntent);
    }

    /**
     * This method finds all the views in the activity
     */
    private void findViews() {
        game_LBL_score = findViewById(R.id.game_LBL_score);
        gameActivity_FAB_left = findViewById(R.id.gameActivity_FAB_left);
        gameActivity_FAB_right = findViewById(R.id.gameActivity_FAB_right);
        gameBoard = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_0_0), findViewById(R.id.game_IMG_0_1), findViewById(R.id.game_IMG_0_2), findViewById(R.id.game_IMG_0_3), findViewById(R.id.game_IMG_0_4)},
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

    /**
     * This method loads the images to the game board
     *
     * @param imageNum  == the image number
     * @param imageView == the image view
     */
    private void loadImage(int imageNum, ShapeableImageView imageView) {
        Glide.with(this).load(imageNum).fitCenter().into(imageView);
    }

    /**
     * This method deletes the image from the game board
     *
     * @param imageView == the image view
     */
    private void deleteImage(ShapeableImageView imageView) {
        Glide.with(this).clear(imageView);
    }

    /**
     * First - we delete the images of the plane from his current location
     * Second - we check if the plane goes right or left and move him
     * Then - we load the plane image to his new location
     *
     * @param direction  = the direction of the plane
     * @param indication = indication if we need to move the plane image
     */
    private void movePlane(String direction, boolean indication) {
        if (indication == true) {
            if (!direction.equals(STAY_IN_PUT)) {
                deleteImage(gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
                if (direction.equals(RIGHT_DIRECTION)) {
                    gameManager.movePlane(STEP_RIGHT_OF_PLANE);
                } else if (direction.equals(LEFT_DIRECTION)) {
                    gameManager.movePlane(STEP_LEFT_OF_PLANE);
                }
            }
            loadImage(gameManager.getPlane().getObjectImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
        }
    }

    /**
     * This method creates the moving plane buttons
     */
    private void createMovingPlaneButtons() {
        gameActivity_FAB_right.setOnClickListener(v -> movePlane(RIGHT_DIRECTION, MOVE_PLANE_IMAGE));
        gameActivity_FAB_left.setOnClickListener(v -> movePlane(LEFT_DIRECTION, MOVE_PLANE_IMAGE));
    }

    /**
     * This method moves the plane
     */
    private void initStepDetector() {
        // Create a SensorManager to listen to step alerts
        movementDetector = new MovementDetector(this, new MovementCallback() {
            @Override
            public void stepRight() {
                movePlane(RIGHT_DIRECTION, MOVE_PLANE_IMAGE);
            }

            @Override
            public void stepLeft() {
                movePlane(LEFT_DIRECTION, MOVE_PLANE_IMAGE);
            }
        });
    }


    /**
     * Clean the game board
     */
    private void cleanTheBoard() {
        for (int i = Y_LENGTH - 1; i > 0; i--) {
            for (int j = 0; j < X_LENGTH; j++) {
                // check if there is plane image in the board
                if (i != 10 || j != gameManager.getPlane().getX()) {
                    deleteImage(gameBoard[i][j]);
                }
            }
        }
    }

    /**
     * This method loads all the objects images to the game board
     */
    private void loadAllObjects() {
        Object[][] board = gameManager.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //Checking if there is exist object in this location and if it reached to the limit he can go out of the board
                if (board[i][j] != null && board[i][j].getY() <= PLANE_LINE + 1) {
                    loadImage(board[i][j].getObjectImage(), gameBoard[board[i][j].getY()][board[i][j].getX()]);
                }
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
        gameManager.moveObjectsDown(PLANE_LINE, X_LENGTH);
        loadAllObjects();
        movePlane(STAY_IN_PUT, DO_NOT_MOVE_PLANE_IMAGE);
    }

    /**
     * This method play the explosion sound when the plane is hit by an bird for the third time
     */
    private void explosionToastAndSound() {
        //Make sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.explosion);
        mediaPlayer.start();
        //Toast
        Toast.makeText(this, "You exploded!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method play the crash sound when the plane is hit by an bird
     */
    private void crashToastAndSound() {
        //Make sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.crowd_panic);
        mediaPlayer.start();
        //Toast
        Toast.makeText(this, "Birds attacked you!", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * This method play the save sound when the plane save passenger
     */
    private void saveSound() {
        //Make sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.relieved_sigh);
        mediaPlayer.start();
    }

    /**
     * Make vibrations
     */
    private void vibrateAll() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void stopTimers() {
        checkingHitTimer.cancel();
        movingObjectsTimer.cancel();
    }

    /**
     * This method refresh the game board and create new bird every 1 second
     */
    private void refreshUI() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        seconds = seconds % 60;

        //Checking if there is objects to move,
        //If it does - move them
        if (gameManager.isEmptyBoard() == false) {
            moveAllObjects();
        }

        //Create new bird every 1 seconds
        if (seconds % 1 == 0) {
            gameManager.createNewBird(-1, X_LENGTH);
        }
    }

    /**
     * This method checks if the plane save paasenger
     * If it does - vibrate, play the save sound, change the score and relaoad the plane image
     */
    public void checkIfSave() {
        if (gameManager.checkIfSave(PLANE_LINE)) {
            vibrateAll();
            saveSound();
            game_LBL_score.setText("" + gameManager.getPlane().getScore());
            deleteImage(gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
            loadImage(gameManager.getPlane().getObjectImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
        }
    }

    /**
     * Happens when the plane hit by an bird for the third time
     * Stop the timers, play the explosion sound, show the toast and move to the game over activity
     */
    private void planeExploded() {
        stopTimers();
        explosionToastAndSound();

        // Delivering the score to the scores screen
        scoresIntent = new Intent(GameActivity.this, ScoresActivity.class);
        scoresIntent.putExtra(ScoresActivity.KEY_SCORE, gameManager.getPlane().getScore());
        startActivity(scoresIntent);

        finish();
    }

    /**
     * Happens when the plane hit by an bird (and he still can play)
     * Drop one heart, play the crash sound, show the toast, clean the board and reload the plane image
     */
    private void birdsHitPlane() {
        game_IMG_hearts[game_IMG_hearts.length - gameManager.getPlane().getNumOfCrash()].setVisibility(View.INVISIBLE);

        crashToastAndSound();

        gameManager.clearAllObjects();

        gameManager.getPlane().setY(PLANE_LINE);
        gameManager.getPlane().setX(DEFAULT_X_FOR_PLANE);
        loadImage(gameManager.getPlane().getObjectImage(), gameBoard[gameManager.getPlane().getY()][gameManager.getPlane().getX()]);
    }

    /**
     * Moving all the objects (except the plane) one step down every DELAY milliseconds (according to the player choice)
     */
    private void startMovingObjectsTimer() {
        movingObjectsTimer = new Timer();
        movingObjectsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    refreshUI();
                });
            }
        }, DELAY, DELAY);
    }

    /**
     * Checking if the plane hit any object every 0.1 seconds
     */
    private void startCheckHitTimer() {
        checkingHitTimer = new Timer();
        checkingHitTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    checkIfSave();
                    if (gameManager.checkIfCrash(PLANE_LINE) == true) {
                        vibrateAll();
                        if (gameManager.getPlane().getNumOfCrash() == gameManager.getPlane().getLife()) {
                            planeExploded();
                        } else if (gameManager.getPlane().getNumOfCrash() != 0) {
                            birdsHitPlane();
                        }
                    } else {
                        cleanTheBoard();
                        loadAllObjects();
                    }
                });
            }
        }, 100, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(BackgroundSoundIntent);
        startCheckHitTimer();
        startMovingObjectsTimer();
        if (buttonsEnabled == false) {
            movementDetector.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(BackgroundSoundIntent);
        stopTimers();
        if (buttonsEnabled == false) {
            movementDetector.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(BackgroundSoundIntent);
        stopTimers();
        if (buttonsEnabled == false) {
            movementDetector.stop();
        }
    }
}