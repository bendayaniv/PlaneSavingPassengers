package com.example.planesavingpassengers.Views.Fragments;

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
import com.example.planesavingpassengers.PlayerAdapter;
import com.example.planesavingpassengers.R;
import com.example.planesavingpassengers.Interfaces.Callback_userProtocol;
import com.example.planesavingpassengers.Utils.MySP;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListFragment extends Fragment {

    ListView scores_LSTVIEW_scoresList;

    private Callback_userProtocol callback;

    private static final String SP_KEY_SCORELIST = "SP_KEY_SCORELIST";

    private ScoreList scoreList = new ScoreList();

    private ScoreList playersListFromSP;
    PlayerAdapter playerAdapter;
    private ArrayList<PlayerModel> playerModelArrayList;
    private String[] playersNames = new String[10];
    private int[] playersScores = new int[10];

    public void setCallback(Callback_userProtocol callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);

        scoreList.setName("Top 10");

        loadFromSP();

        playerModelArrayList = populateList();
        playerAdapter = new PlayerAdapter(getContext(), playerModelArrayList);

        if (playerModelArrayList.size() > 0) {
            scores_LSTVIEW_scoresList.setAdapter(playerAdapter);
        }

        scores_LSTVIEW_scoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickOnItemInList(position);
            }
        });

        return view;
    }

    /**
     * This method is called when the user clicks on an item in the list
     *
     * @param position == the position of the item in the list
     */
    private void clickOnItemInList(int position) {
        if (callback != null) {
            showLocationOnMap(scoreList.getPlayers().get(position).getLatitude(), scoreList.getPlayers().get(position).getLongitude());
        }
    }

    /**
     * This method is called when the user clicks on an item in the list, and send the details to the map fragment
     *
     * @param latitude  == the latitude of the item in the list
     * @param longitude == the longitude of the item in the list
     */
    private void showLocationOnMap(double latitude, double longitude) {
        if (callback != null) {
            callback.sendLocation(latitude, longitude);
        }
    }

    /**
     * This method is called when the fragment is created, and send the list of top 10 players to the map fragment
     *
     * @param players == the list of top 10 players
     */
    private void showAllLocationsOnMap(ArrayList<Player> players) {
        if (callback != null) {
            callback.sendAllTop10Locations(players);
        }
    }


    /**
     * This method is called when the fragment is created, and find the views in the fragment
     *
     * @param view == the view of the fragment
     */
    private void findViews(View view) {
        scores_LSTVIEW_scoresList = (ListView) view.findViewById(R.id.scores_LSTVIEW_scoresList);
    }

    /**
     * This method get the details of the new player, and add it to the list of top 10 players
     * Then it sort the list by the score of the players
     * Then it update the list in the fragment
     *
     * @param player == the new player
     */
    public void getDetails(Player player) {
        scoreList.getPlayers().add(player);
        sortScoreListByScore(scoreList.getPlayers());
        if (scoreList.getPlayers().size() > 10) {
            scoreList.getPlayers().remove(10);
        }
        refreshNameAndScoreArrays();

//        PlayerModel playerModel = new PlayerModel();
//        playerModel.setName(player.getName());
//        playerModel.setScore(player.getScore());
//        playerModelArrayList.add(playerModel);
//        playerModelArrayList = sortPlayerModelArrayListByScore(playerModelArrayList);
////        playerModelArrayList = populateList();
//
//        if (playerModelArrayList.size() != 0) {
//            scores_LSTVIEW_scoresList.setAdapter(playerAdapter);
//        }
//        if (playerAdapter != null) {
//            playerAdapter.notifyDataSetChanged();
//        }

        playerModelArrayList = populateList();
        playerAdapter = new PlayerAdapter(getContext(), playerModelArrayList);
        if (playerModelArrayList.size() > 0) {
            scores_LSTVIEW_scoresList.setAdapter(playerAdapter);
        }
        if (playerAdapter != null) {
            playerAdapter.notifyDataSetChanged();
        }

        showAllLocationsOnMap(scoreList.getPlayers());

//        saveToSP();
//        loadFromSP();
    }

    /**
     * This method refresh the lists of the names and scores so only the top 10 players will be shown
     */
    private void refreshNameAndScoreArrays() {
        int j = scoreList.getPlayers().size();
        if (scoreList.getPlayers().size() > 10) {
            j = 10;
        }
        for (int i = 0; i < j; i++) {
            playersNames[i] = scoreList.getPlayers().get(i).getName();
            playersScores[i] = scoreList.getPlayers().get(i).getScore();
        }
    }

    /**
     * This method sort the list of top 10 players by the score of the players
     *
     * @param players == the list of top 10 players
     */
    private void sortScoreListByScore(ArrayList<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                int score1 = p1.getScore();
                int score2 = p2.getScore();
                if (score1 == score2)
                    return 0;
                else if (score1 < score2)
                    return 1;
                else
                    return -1;
            }
        });
    }

    /**
     * This method sort the list of top 10 players by the score of the players
     *
     * @param list == the list of top 10 players
     * @return == the sorted list of top 10 players
     */
    private ArrayList<PlayerModel> sortPlayerModelArrayListByScore(ArrayList<PlayerModel> list) {
        Collections.sort(list, new Comparator<PlayerModel>() {
            @Override
            public int compare(PlayerModel p1, PlayerModel p2) {
                int score1 = p1.getScore();
                int score2 = p2.getScore();
                if (score1 == score2)
                    return 0;
                else if (score1 < score2)
                    return 1;
                else
                    return -1;
            }
        });
        return list;
    }

    /**
     * This method populate the list of top 10 players
     *
     * @return == the list of top 10 players
     */
    private ArrayList<PlayerModel> populateList() {
        ArrayList<PlayerModel> list = new ArrayList<>();
        int j = checkLengthOfTop10List();
        if (j > 10) {
            j = 10;
        }
        if (j != 0) {
            for (int i = 0; i < j; i++) {
                PlayerModel playerModel = new PlayerModel();
                playerModel.setName(playersNames[i]);
                playerModel.setScore(playersScores[i]);
                list.add(playerModel);
            }
            list = sortPlayerModelArrayListByScore(list);
        }
        return list;
    }

    /**
     * This method check the length of the list of top 10 players
     *
     * @return == the length of the list of top 10 players
     */
    private int checkLengthOfTop10List() {
        int counter = 0;
        for (int i = 0; i < playersNames.length; i++) {
            if (playersNames[i] != null) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Load the score list from shared preferences
     */
    private void loadFromSP() {
        int i = 0;
        String playersListAsJsonStringFromSP = MySP.getInstance().getString(SP_KEY_SCORELIST, "");

        if (!playersListAsJsonStringFromSP.isEmpty()) {
            playersListFromSP = new Gson().fromJson(playersListAsJsonStringFromSP, ScoreList.class);
            playersListFromSP.setName("Top 10");
            if (playersListFromSP.getPlayers().size() > 10) {
                i = 10;
            } else {
                i = playersListFromSP.getPlayers().size();
            }
            for (int j = 0; j < i; j++) {
                playersNames[j] = playersListFromSP.getPlayers().get(j).getName();
                playersScores[j] = playersListFromSP.getPlayers().get(j).getScore();
                scoreList.getPlayers().add(playersListFromSP.getPlayers().get(j));
            }
//            refreshNameAndScoreArrays();
            showAllLocationsOnMap(scoreList.getPlayers());
        }
    }

    /**
     * Save the score list to shared preferences
     */
    public void saveToSP() {
        String playersListJson = new Gson().toJson(scoreList);
        MySP.getInstance().putString(SP_KEY_SCORELIST, playersListJson);
    }
}