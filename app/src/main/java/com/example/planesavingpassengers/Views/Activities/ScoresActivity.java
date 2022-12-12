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

public class ScoresActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";
    public static final String INDICATION = "INDICATION";

    private int score = 0;
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

    Callback_userProtocol callback_userProtocol = new Callback_userProtocol() {
        @Override
        public void sendLocation(double latitude, double longitude) {
//            showUserLocation(latitude, longitude);
            mapFragment.zoom(latitude, longitude);
        }
    };

    private void showUserLocation(double latitude, double longitude) {
        mapFragment.zoom(latitude, longitude);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        findViews();

        buttons();

        createFragments();

//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(2000);

        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra(KEY_SCORE, 0);
        indication = prevIntent.getBooleanExtra(INDICATION, true);

//        if (indication == true) {
//            hideMapAndList();
//        } else
        if (indication == false) {
            mapAndScoreVisible();
        }

        getCurrentLocation();
    }

    private void createFragments() {
        listFragment = new ListFragment();
        listFragment.setCallback(callback_userProtocol);

        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_map, mapFragment).commit();
    }

    private void buttons() {
        scores_BTN_saveScore.setOnClickListener(v -> {
            if (/*currentLatitude == 0 && currentLongitude == 0*/isGPSEnabled() == false) {
                Toast.makeText(this, "Need to turn on your GPS", Toast.LENGTH_SHORT).show();
//                return;
            } else {
                listFragment.getDetails(/*scores_EDT_name.getText().toString(), score, */currentLatitude, currentLongitude);
                mapAndScoreVisible();
            }
        });
        scores_BTN_backToMenu.setOnClickListener(v -> backToMenu());
    }

    private void findViews() {
        scores_EDT_name = findViewById(R.id.scores_EDT_name);
        scores_BTN_saveScore = findViewById(R.id.scores_BTN_saveScore);

        scores_LBL_headline = findViewById(R.id.scores_LBL_headline);
        scores_FRAM_list = findViewById(R.id.scores_FRAM_list);
        scores_VIEW_break = findViewById(R.id.scores_VIEW_break);
        scores_FRAM_map = findViewById(R.id.scores_FRAM_map);
        scores_BTN_backToMenu = findViewById(R.id.scores_BTN_backToMenu);
    }

    private void hideMapAndList() {
        if (indication == true) {
            scores_LBL_headline.setVisibility(View.INVISIBLE);
            scores_FRAM_list.setVisibility(View.INVISIBLE);
            scores_VIEW_break.setVisibility(View.INVISIBLE);
            scores_FRAM_map.setVisibility(View.INVISIBLE);
            scores_BTN_backToMenu.setVisibility(View.INVISIBLE);
        } else if (indication == false) {
            mapAndScoreVisible();
        }
    }

    private void mapAndScoreVisible() {
        getCurrentLocation();

//        Toast.makeText(this, "Latitude: " + currentLatitude, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Longitude: " + currentLongitude, Toast.LENGTH_SHORT).show();

//        listFragment.getDetails(/*scores_EDT_name.getText().toString(), score, */currentLatitude, currentLongitude);
//        listFragment.getDetails(scores_EDT_name.getText().toString(), score, 32.5, 22.1);


        scores_EDT_name.setVisibility(View.INVISIBLE);
        scores_BTN_saveScore.setVisibility(View.INVISIBLE);

        scores_LBL_headline.setVisibility(View.VISIBLE);
        scores_FRAM_list.setVisibility(View.VISIBLE);
        scores_VIEW_break.setVisibility(View.VISIBLE);
        scores_FRAM_map.setVisibility(View.VISIBLE);
        scores_BTN_backToMenu.setVisibility(View.VISIBLE);
    }

    private void backToMenu() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
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


    public void getCurrentLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
//        LocationResult locationResult;
//        locationResult.getLocations().lastIndexOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Toast.makeText(ScoresActivity.this, "0", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(ScoresActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(ScoresActivity.this, "1", Toast.LENGTH_SHORT).show();
                if (isGPSEnabled()) {
//                    Toast.makeText(ScoresActivity.this, "2", Toast.LENGTH_SHORT).show();
                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
//                                    Toast.makeText(ScoresActivity.this, "3", Toast.LENGTH_SHORT).show();

                                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        currentLatitude = locationResult.getLocations().get(index).getLatitude();
                                        currentLongitude = locationResult.getLocations().get(index).getLongitude();
//                                        listFragment.getDetails(scores_EDT_name.getText().toString(),
//                                                score, 0, 0);
                                    }
                                }
                            }, Looper.getMainLooper());
//                    Toast.makeText(ScoresActivity.this, "4", Toast.LENGTH_SHORT).show();
                } else {
                    turnOnGPS(ScoresActivity.this);
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


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

    // Check if GPS is enabled
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