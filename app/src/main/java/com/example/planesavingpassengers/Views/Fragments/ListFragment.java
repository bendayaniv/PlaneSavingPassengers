package com.example.planesavingpassengers.Views.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Interfaces.Callback_userProtocol;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    ListView scores_LSTVIEW_scoresList;
    ArrayList<String> scoresList;
    ArrayAdapter arrayAdapter;

//    double currentLatitude = 0;
//    double currentLongitude = 0;

    private Callback_userProtocol callback;

    public void setCallback(Callback_userProtocol callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);

        scoresList = new ArrayList<>();
//        scoresList.add("1");
//        scoresList.add("2");
//        scoresList.add("3");
//        scoresList.add("4");
//        scoresList.add("5");
//        scoresList.add("6");
//        scoresList.add("7");
//        scoresList.add("8");
//        scoresList.add("9");
//        scoresList.add("10");

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, scoresList);

        scores_LSTVIEW_scoresList.setAdapter(arrayAdapter);

        scores_LSTVIEW_scoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    String[] stringArray = scoresList.get(position).trim().split("\\s+");//remove any leading, trailing white spaces and split the string from rest of the white spaces

                    double[] intArray = new double[stringArray.length];//create a new double array to store the int values

                    for (int i = 0; i < stringArray.length; i++) {
                        intArray[i] = new Double(stringArray[i]);//parse the integer value and store it in the int array
                    }
                    showLocationOnMap(intArray[0], intArray[1]);
                }
            }
        });

        return view;
    }

    private void showLocationOnMap(double latitude, double longitude) {
        if (callback != null) {
            callback.sendLocation(latitude, longitude);
        }
    }


    private void findViews(View view) {
        scores_LSTVIEW_scoresList = (ListView) view.findViewById(R.id.scores_LSTVIEW_scoresList);
    }

//    private void showLocationOnMap() {
//        if (callback != null) {
//            callback.sendLocation(currentLatitude, currentLongitude);
//        }
//    }

    public void getDetails(/*String name, int score, */double latitude, double longitude) {
        scoresList.add(/*name + " " + score + " " + */latitude + " " + longitude);
//        currentLatitude = latitude;
//        currentLongitude = longitude;
//        showLocationOnMap();
        arrayAdapter.notifyDataSetChanged();
    }
}