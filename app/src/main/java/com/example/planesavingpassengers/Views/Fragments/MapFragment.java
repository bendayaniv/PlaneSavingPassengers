package com.example.planesavingpassengers.Views.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planesavingpassengers.Models.Objects.Player;
import com.example.planesavingpassengers.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    ArrayList<Player> players;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_FCV_map);

        mapFragment.getMapAsync(this);

        return view;
    }

    /**
     * Add marker to the map
     *
     * @param lat == latitude
     * @param lng == longitude
     */
    public void addMarkOnMap(double lat, double lng) {
        LatLng randomPlace = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(randomPlace));
    }

    /**
     * Move the camera to the given location
     *
     * @param _latitude  == latitude
     * @param _longitude == longitude
     */
    public void zoom(double _latitude, double _longitude) {
        LatLng randomPlace = new LatLng(_latitude, _longitude);
//
        // Construct a CameraPosition focusing on randomPlace and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(randomPlace)      // Sets the center of the map to randomPlace
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Initialize the list of players so in the future we will can add marks on the map
     *
     * @param players == list of players on the top 10 list
     */
    public void updatePlayersList(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Add markers on the map
     */
    public void refreshMarkerOnMap() {
        mMap.clear();
        for (Player player : players) {
//            LatLng randomPlace = new LatLng(player.getLatitude(), player.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(randomPlace));
            addMarkOnMap(player.getLatitude(), player.getLongitude());
        }
    }

    /**
     * This function is called when the map is ready to be used.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (players != null) {
            refreshMarkerOnMap();
//            for (Player player : players) {
//                addMarkOnMap(player.getLatitude(), player.getLongitude());
//            }
        }
    }


}