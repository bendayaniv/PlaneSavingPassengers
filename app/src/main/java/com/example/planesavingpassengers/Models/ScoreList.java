package com.example.planesavingpassengers.Models;

import com.example.planesavingpassengers.Models.Objects.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreList {

    private ArrayList<Player> players = new ArrayList<>();

    public ScoreList() {
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ScoreList setPlayers(ArrayList<Player> players) {
        this.players = players;
        return this;
    }

    /**
     * This method sort the list of top 10 players by the score of the players
     */
    public void sortListByScore() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getScore() - p1.getScore();
            }
        });
    }


    @Override
    public String toString() {
        return "ScoreList{" +
                ", players=" + players +
                '}';
    }
}
