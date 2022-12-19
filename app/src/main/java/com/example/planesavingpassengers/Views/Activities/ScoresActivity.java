package com.example.planesavingpassengers.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

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

import com.example.planesavingpassengers.Models.Objects.Player;
import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.Views.Fragments.ListFragment;
import com.example.planesavingpassengers.Views.Fragments.MapFragment;
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
    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String INDICATION = "INDICATION";

    private int score = 0;
    // Indication for if moving the plane image or not
    public boolean indication = true;

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
    private static final int REQUEST_CHECK_SETTINGS = 10001;
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
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra(KEY_SCORE, 0);
        indication = prevIntent.getBooleanExtra(INDICATION, true);

        if (indication == false) {
            hideSaveScore();
        }

        getCurrentLocation();
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

    /**
     * This method sets the buttons in the activity
     */
    private void buttons() {
        scores_BTN_saveScore.setOnClickListener(v -> saveScore());
        scores_BTN_backToMenu.setOnClickListener(v -> backToMenu());
    }

    /**
     * This method is responsible for saving the score
     */
    private void saveScore() {
        // TODO: To deal with the case that if the user don't get permission to his location
        if (scores_EDT_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (isGPSEnabled() == false) {
            Toast.makeText(this, "Need to turn on your GPS", Toast.LENGTH_SHORT).show();
            turnOnGPS(ScoresActivity.this);
            if (isGPSEnabled() == true) {
                getCurrentLocation();
            }
        } else {
            getCurrentLocation();
            // The default location is (0, 0), and it is in the middle of the ocean, and it is not a valid location,
            // so I wait for the location to be updated
            if (currentLatitude == 0 && currentLongitude == 0) {
                Toast.makeText(this, "Need to sync", Toast.LENGTH_SHORT).show();
                return;
            }
            listFragment.getDetails(new Player(scores_EDT_name.getText().toString(), score, currentLatitude, currentLongitude));
            mapFragment.refreshMarkerOnMap();
            hideSaveScore();
        }
    }

    /**
     * This method is responsible for going back to the menu
     */
    private void backToMenu() {
        listFragment.saveToSP();
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
     * This method is responsible for hiding the save score
     */
    private void hideSaveScore() {
        scores_EDT_name.setVisibility(View.INVISIBLE);
        scores_BTN_saveScore.setVisibility(View.INVISIBLE);

//        scores_LBL_headline.setVisibility(View.VISIBLE);
//        scores_FRAM_list.setVisibility(View.VISIBLE);
//        scores_VIEW_break.setVisibility(View.VISIBLE);
//        scores_FRAM_map.setVisibility(View.VISIBLE);
//        scores_BTN_backToMenu.setVisibility(View.VISIBLE);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                if (isGPSEnabled()) {
//
//                    getCurrentLocation();
//
//                } else {
//
//                    turnOnGPS(ScoresActivity.this);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 2) {
//            if (resultCode == Activity.RESULT_OK) {
//
//                getCurrentLocation();
//            }
//        }
//    }

    // The next 3 methods are for getting the current location of the user:

    /**
     * This method is responsible for getting the current location of the user
     */
    public void getCurrentLocation() {
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the user gave permission to his location
            if (ActivityCompat.checkSelfPermission(ScoresActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Check if the GPS is enabled
                if (isGPSEnabled()) {
                    // Get the location
                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        currentLatitude = locationResult.getLocations().get(index).getLatitude();
                                        currentLongitude = locationResult.getLocations().get(index).getLongitude();
                                    }
                                }
                            }, Looper.getMainLooper());
                    ;
                }
                // If the GPS is not enabled
                else {
                    turnOnGPS(ScoresActivity.this);
                }
            }
            // If the user didn't give permission to his location
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    /**
     * This method is responsible for checking if the GPS is enabled
     *
     * @param context == the context of the activity
     */
    private void turnOnGPS(Context context) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context.getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(context, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
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
        LocationManager locationManager = null;
        boolean isGPSenabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSenabled;
    }
}