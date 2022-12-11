package com.example.planesavingpassengers.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planesavingpassengers.R;
import com.google.android.material.textview.MaterialTextView;

public class MapFragment extends Fragment {

    private MaterialTextView map_LBL_title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        map_LBL_title = view.findViewById(R.id.map_LBL_title);
    }

    public void zoom(double latitude, double longitude) {
        map_LBL_title.setText(latitude + "\n" + longitude);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_map);
//    }
}