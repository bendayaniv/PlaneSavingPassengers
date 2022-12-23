package com.example.planesavingpassengers.Controller.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import com.example.planesavingpassengers.Interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.Models.Objects.Player;
import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Controller.Fragments.ListFragment;
import com.example.planesavingpassengers.Controller.Fragments.MapFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String INDICATION = "INDICATION"; // An indication of where we have come from
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    private static final int REQUEST_CODE = 10002;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private int score = 0;

    private MaterialTextView scores_LBL_headline;
    private FrameLayout scores_FRAM_list;
    private View scores_VIEW_break;
    private FrameLayout scores_FRAM_map;
    private MaterialButton scores_BTN_backToMenu;

    private ListFragment listFragment;
    private MapFragment mapFragment;

    private AppCompatEditText scores_EDT_name;
    private MaterialButton scores_BTN_saveScore;

    private LocationRequest locationRequest;

    double currentLatitude = 0;
    double currentLongitude = 0;

    /**
     * This callback is created when the activity is created
     */
    Callback_userProtocol callback_userProtocol = new Callback_userProtocol() {
        @Override
        public void sendLocation(double latitude, double longitude) {
            mapFragment.zoom(latitude, longitude);
        }

        @Override
        public void sendAllTop10Locations(ArrayList<Player> players) {
            mapFragment.updatePlayersList(players);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        findViews();

        buttons();

        createFragments();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(200);

        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra(KEY_SCORE, 0);

        // Indication for if moving the plane image or not
        if (prevIntent.getBooleanExtra(INDICATION, true) == false) {
            hideSaveScore();
        }
    }

    /**
     * This method is responsible for checking if the GPS is enabled
     *
     * @param context == the context of the activity
     */
    private void turnOnGPS(Context context) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context.getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(context, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult((Activity) context, /*2*/REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    /**
     * This method is responsible for checking if the GPS is enabled
     *
     * @return == true if the GPS is enabled, false otherwise
     */
    private boolean isGPSEnabled() {
        // Initialize the location manager
        LocationManager locationManager = null;
        boolean isGPSEnabled = false;
        if (locationManager == null) {
            // Get the system service of the location
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        // Check if the GPS is provided
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }


    // Belong to the getCurrentLocation method
    // Activated automatically as soon as the user is requested to allow the app to use his location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == /*1*/REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation();
                } else {
                    turnOnGPS(ScoresActivity.this);
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    // Belong to the turnOnGPS method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == /*2*/REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation();
            }
        }
    }

    /**
     * This method is responsible for getting the current location of the user
     */
    public /*static*/ void getCurrentLocation() {
        // Check for the user permission to get the location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the user gave permission to his location
            if (ActivityCompat.checkSelfPermission(ScoresActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Check if the GPS is enabled
                if (isGPSEnabled()) {
                    // Get the current location
                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            // Only need the last location
                            LocationServices.getFusedLocationProviderClient(ScoresActivity.this).removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int index = locationResult.getLocations().size() - 1;
                                currentLatitude = locationResult.getLocations().get(index).getLatitude();
                                currentLongitude = locationResult.getLocations().get(index).getLongitude();
                            }
                        }
                    }, Looper.getMainLooper());
                }
                // If the GPS is not enabled
                else {
                    turnOnGPS(ScoresActivity.this);
                }
            }
            // If the user didn't give permission to his location
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, /*1*/REQUEST_CODE);
            }
        }
    }

    /**
     * This method is responsible for hiding the save score
     */
    private void hideSaveScore() {
        scores_EDT_name.setVisibility(View.INVISIBLE);
        scores_BTN_saveScore.setVisibility(View.INVISIBLE);
    }

    /**
     * This method is responsible for saving the score
     */
    private void saveScore() {
        if (scores_EDT_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please allow the app to use your location", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, /*1*/REQUEST_CODE);
        } else if (isGPSEnabled() == false) {
            Toast.makeText(this, "Need to turn on your GPS", Toast.LENGTH_SHORT).show();
            turnOnGPS(ScoresActivity.this);
        } else {
            getCurrentLocation();
            // The default location at first is (0, 0), and it is in the middle of the ocean, and it is not a valid location
            // (unless the player is a marine explorer in the middle of the ocean, and he probably won't really have internet for that),
            // so we wait for the location to be updated
            if (currentLatitude == 0 && currentLongitude == 0) {
                Toast.makeText(this, "Need to sync", Toast.LENGTH_SHORT).show();
                return;
            }
            if (listFragment.putInTop10(new Player(scores_EDT_name.getText().toString(), score, currentLatitude, currentLongitude)) == false) {
                Toast.makeText(this, "Sorry pal, not good enough for our TOP10", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You did it to the TOP10!", Toast.LENGTH_SHORT).show();
                mapFragment.refreshMarkerOnMap();
            }
            hideSaveScore();
        }
    }

    /**
     * This method is responsible for going back to the menu
     */
    private void backToMenu() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }

    /**
     * This method is responsible for finding the views in the activity
     */
    private void findViews() {
        scores_EDT_name = findViewById(R.id.scores_EDT_name);
        scores_BTN_saveScore = findViewById(R.id.scores_BTN_saveScore);

        scores_LBL_headline = findViewById(R.id.scores_LBL_headline);
        scores_FRAM_list = findViewById(R.id.scores_FRAM_list);
        scores_VIEW_break = findViewById(R.id.scores_VIEW_break);
        scores_FRAM_map = findViewById(R.id.scores_FRAM_map);
        scores_BTN_backToMenu = findViewById(R.id.scores_BTN_backToMenu);
    }

    /**
     * This method sets the buttons in the activity
     */
    private void buttons() {
        scores_BTN_saveScore.setOnClickListener(v -> saveScore());
        scores_BTN_backToMenu.setOnClickListener(v -> backToMenu());
    }

    /**
     * This method is responsible for creating the fragments
     */
    private void createFragments() {
        listFragment = new ListFragment();
        listFragment.setCallback(callback_userProtocol);

        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_map, mapFragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }
}