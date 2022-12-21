package com.example.planesavingpassengers.Controller.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.planesavingpassengers.Models.Objects.Player;
import com.example.planesavingpassengers.Models.PlayerModel;
import com.example.planesavingpassengers.Models.ScoreList;
import com.example.planesavingpassengers.Adapters.PlayerAdapter;
import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.Utils.MySP;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    ListView scores_LSTVIEW_scoresList;

    private Callback_userProtocol callback;

    private ScoreList scoreList = new ScoreList();
    PlayerAdapter playerAdapter;

    public void setCallback(Callback_userProtocol callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);

        loadFromSP();

        initPlayerAdapter();

        return view;
    }

    /**
     * This method is called when the fragment is created, and find the views in the fragment
     *
     * @param view == the view of the fragment
     */
    private void findViews(View view) {
        scores_LSTVIEW_scoresList = (ListView) view.findViewById(R.id.scores_LSTVIEW_scoresList);
        scores_LSTVIEW_scoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (callback != null) {
                    callback.sendLocation(scoreList.getPlayers().get(position).getLatitude(), scoreList.getPlayers().get(position).getLongitude());
                }
            }
        });
    }

    /**
     * This method populate the list of top 10 players
     *
     * @return == the list of top 10 players
     */
    private ArrayList<PlayerModel> populateList() {
        ArrayList<PlayerModel> list = new ArrayList<>();
        int j = scoreList.getPlayers().size();
        if (j > 10) {
            j = 10;
        }
        if (j != 0) {
            for (int i = 0; i < j; i++) {
                list.add(new PlayerModel()
                        .setName(scoreList.getPlayers().get(i).getName())
                        .setScore(scoreList.getPlayers().get(i).getScore()));
            }
        }
        return list;
    }

    /**
     * Init the player adapter, and set the list view adapter
     */
    private void initPlayerAdapter() {
        playerAdapter = new PlayerAdapter(getContext(), populateList());

        if (playerAdapter.getPlayerModelArrayList().size() > 0) {
            scores_LSTVIEW_scoresList.setAdapter(playerAdapter);
        }

        if (playerAdapter != null) {
            playerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method is called when the fragment is created, and send the list of top 10 players to the map fragment
     *
     * @param players == the list of top 10 players
     */
    private void sendThePlayerList(ArrayList<Player> players) {
        if (callback != null) {
            callback.sendAllTop10Locations(players);
        }
    }

    /**
     * Load the score list from shared preferences
     */
    private void loadFromSP() {
        scoreList = MySP.loadFromSP(ScoreList.class, scoreList);
        if (!scoreList.getPlayers().isEmpty()) {
            sendThePlayerList(scoreList.getPlayers());
        }
    }

    /**
     * Save the score list to shared preferences
     */
    public void saveToSP() {
        MySP.saveToSP(scoreList);
        sendThePlayerList(scoreList.getPlayers());
    }

    /**
     * This method put the new player in the list,
     * sort the list by score,
     * remove the last player if the list is bigger than 10 (the 11th player)
     * and save the list in the shared preferences
     *
     * @param player == the new player
     * @return == if the new player is in the top 10
     */
    public boolean putInTop10(Player player) {
        boolean inTop10 = false;
        scoreList.getPlayers().add(player);
        scoreList.sortListByScore();

        if (scoreList.getPlayers().size() > 10 && scoreList.getPlayers().get(10) == player) {
            scoreList.getPlayers().remove(10);
        } else {
            inTop10 = true;
        }

        initPlayerAdapter();

        saveToSP();

        return inTop10;
    }
}