package com.example.planesavingpassengers.View.Activity;

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
import com.example.planesavingpassengers.interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.View.Fragments.ListFragment;
import com.example.planesavingpassengers.View.Fragments.MapFragment;
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
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    double latitude = 0;
    double longitude = 0;

    Callback_userProtocol callback_userProtocol = new Callback_userProtocol() {
        @Override
        public void sendLocation(double latitude, double longitude) {
            showUserLocation(latitude, longitude);
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

        hideMapAndList();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        Intent prevIntent = getIntent();
        score = prevIntent.getIntExtra(KEY_SCORE, 0);
    }

    private void createFragments() {
        listFragment = new ListFragment();
        listFragment.setCallback(callback_userProtocol);

        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAM_map, mapFragment).commit();
    }

    private void buttons() {
        scores_BTN_saveScore.setOnClickListener(v -> mapAndScoreVisible());
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
        scores_LBL_headline.setVisibility(View.INVISIBLE);
        scores_FRAM_list.setVisibility(View.INVISIBLE);
        scores_VIEW_break.setVisibility(View.INVISIBLE);
        scores_FRAM_map.setVisibility(View.INVISIBLE);
        scores_BTN_backToMenu.setVisibility(View.INVISIBLE);
    }

    private void mapAndScoreVisible() {
        getCurrentLocation();

        listFragment.getDetails(scores_EDT_name.getText().toString(), score, latitude, longitude);

        scores_EDT_name.setVisibility(View.GONE);
        scores_BTN_saveScore.setVisibility(View.GONE);

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


    public void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(ScoresActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(ScoresActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();
                                    }
                                }
                            }, Looper.getMainLooper());
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