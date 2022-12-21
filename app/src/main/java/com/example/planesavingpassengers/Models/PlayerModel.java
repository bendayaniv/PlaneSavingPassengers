package com.example.planesavingpassengers.Models;

public class PlayerModel {

    private String name;
    private int score;

    public PlayerModel() {
    }

    public String getName() {
        return name;
    }

    public PlayerModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public PlayerModel setScore(int score) {
        this.score = score;
        return this;
    }
}
