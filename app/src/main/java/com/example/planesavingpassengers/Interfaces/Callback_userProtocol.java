package com.example.planesavingpassengers.Interfaces;

import com.example.planesavingpassengers.Models.Objects.Player;

import java.util.ArrayList;

public interface Callback_userProtocol {
    void sendLocation(double latitude, double longitude);
    void sendAllTop10Locations(ArrayList<Player> players);
}
