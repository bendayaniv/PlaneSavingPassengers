package com.example.planesavingpassengers;

public class Plane extends Object {
    private int numOfCrash = 0;
    private final int life;
    private final int explodeImage = R.drawable.explosion;

    public Plane(int lifeLength, int defaultX, int defaultY) {
        super(defaultX, defaultY, R.drawable.plane);
        life = lifeLength;
    }

    public int getExplodeImage() {
        return explodeImage;
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
}
