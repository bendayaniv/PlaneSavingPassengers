package com.example.planesavingpassengers.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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

import androidx.appcompat.app.AppCompatActivity;

public class Location extends AppCompatActivity {

    private static Location instance = null;
    private Context context;

    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    private static double currentLatitude;
    private static double currentLongitude;

//    public Location() {
//    }

    private Location(Context _context) {
        this.context = _context;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
//        getCurrentLocation();
    }

    public static void init(Context _context) {
        if (instance == null) {
            instance = new Location(_context);
        }
    }

    public static Location getInstance() {
        return instance;
    }

    public static boolean checkIfGPSIsEnable() {
        Toast.makeText(instance.context, "checkIfGPSIsEnable", Toast.LENGTH_SHORT).show();
        boolean GPSIsEnable = instance.isGPSEnabled();

        return GPSIsEnable;
    }

    public static void goToTurnOnGPS() {
        getInstance().turnOnGPS();
    }

    public static void goToGetCurrentLocation() {
        getInstance().getCurrentLocation();
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public static Location setCurrentLatitude(double latitude) {
        /*this.*/
        currentLatitude = latitude;
        return instance;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public static Location setCurrentLongitude(double longitude) {
        /*this.*/
        currentLongitude = longitude;
        return instance;
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                if (isGPSEnabled()) {
//
//                    getCurrentLocation();
//
//                } else {
//
//                    turnOnGPS();
//                }
//            }
//        }
//    }


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

//    public static void getLocation() {
//        if (instance == null) {
//            return;
//        }
//        instance.getCurrentLocation();
//    }


    public void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(context)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(context)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        setCurrentLatitude(locationResult.getLocations().get(index).getLatitude()) /*currentLatitude = locationResult.getLocations().get(index).getLatitude()*/;
                                        setCurrentLongitude(locationResult.getLocations().get(index).getLongitude()) /*currentLongitude = locationResult.getLocations().get(index).getLongitude()*/;
//                                        listFragment.getDetails(scores_EDT_name.getText().toString(),
//                                                score, 0, 0);
                                    }
                                }
                            }, Looper.getMainLooper());
                    ;
                } else {
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    private void turnOnGPS(/*Context context*/) {
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
                                resolvableApiException.startResolutionForResult((Activity) context, /*REQUEST_CHECK_SETTINGS*/2);
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
    /*private*/
    public boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isGPSenabled = false;
        Toast.makeText(context, "10", Toast.LENGTH_SHORT).show();
        if (locationManager == null) {
            Toast.makeText(context, "20", Toast.LENGTH_SHORT).show();
            locationManager = (LocationManager) getSystemService(/*Context*/context.LOCATION_SERVICE);
            Toast.makeText(context, "30", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "40", Toast.LENGTH_SHORT).show();
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Toast.makeText(context, "50", Toast.LENGTH_SHORT).show();
        return isGPSenabled;
    }
}
