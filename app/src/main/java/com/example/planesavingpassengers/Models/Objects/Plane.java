package com.example.planesavingpassengers.Models.Objects;

import com.example.planesavingpassengers.R;

public class Plane extends Object {
    private int numOfCrash = 0;
    private int score = 0;
    private final int life;

    public Plane(int lifeLength, int defaultX, int defaultY) {
        super(defaultX, defaultY, R.drawable.plane);
        life = lifeLength;
    }

    public int getNumOfCrash() {
        return numOfCrash;
    }

    public Plane setNumOfCrash(int numOfCrash) {
        this.numOfCrash = numOfCrash;
        return this;
    }

    public int getLife() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public void setScore(/*int addingScore*/) {
        this.score += 10;
    }

    public void resetScoreForNewGame() {
        this.score = 0;
    }
}
