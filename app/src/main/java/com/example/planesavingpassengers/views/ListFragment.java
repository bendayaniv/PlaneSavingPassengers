package com.example.planesavingpassengers.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.interfaces.Callback_userProtocol;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    ListView scores_LSTVIEW_scoresList;
    ArrayList<String> scoresList;
    ArrayAdapter arrayAdapter;

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
        scoresList.add("1");
        scoresList.add("2");
        scoresList.add("3");
        scoresList.add("4");
        scoresList.add("5");
        scoresList.add("6");
        scoresList.add("7");
        scoresList.add("8");
        scoresList.add("9");

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, scoresList);

        scores_LSTVIEW_scoresList.setAdapter(arrayAdapter);

        scores_LSTVIEW_scoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    showLocationOnMap();
                }
            }
        });

        return view;
    }

    private void findViews(View view) {
        scores_LSTVIEW_scoresList = (ListView) view.findViewById(R.id.scores_LSTVIEW_scoresList);
    }

    private void showLocationOnMap() {
        if (callback != null) {
            callback.sendLocation(0, 0);
        }
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_list);
//    }
}