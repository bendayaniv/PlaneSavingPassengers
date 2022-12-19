package com.example.planesavingpassengers.Models;

import com.example.planesavingpassengers.Models.Objects.Player;

import java.util.ArrayList;

public class ScoreList {
    private String name;
    private ArrayList<Player> players = new ArrayList<>();

    public ScoreList() {
    }

    public String getName() {
        return name;
    }

    public ScoreList setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ScoreList setPlayers(ArrayList<Player> players) {
        this.players = players;
        return this;
    }

    @Override
    public String toString() {
        return "ScoreList{" +
                "name='" + name + '\'' +
                ", players=" + players +
                '}';
    }

    public void add(Player player) {
        players.add(player);
    }
}
