package com.example.planesavingpassengers.Models.Objects;

public class Player {
    private String name;
    private int score;
    private double latitude;
    private double longitude;

    public Player() {
    }

    public Player(String _name, int _score, double _latitude, double _longitude) {
        this.name = _name;
        this.score = _score;
        this.latitude = _latitude;
        this.longitude = _longitude;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Player setLatitude(int latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Player setLongitude(int longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

