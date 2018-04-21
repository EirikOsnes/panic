package com.panic.tdt4240.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * An object to hold lobby info. Values should only be changed by Connection.
 */

public class Lobby {

    private int maxPlayers = 4;
    private String lobbyname;
    private int lobbyID;
    private String mapID;
    private ArrayList<Integer> playerIDs; // ConnectionIDs?
    private ArrayList<String> vehicleTypes;

    public Lobby(int maxPlayers, String lobbyname, int lobbyID, @NonNull String mapID) {
        this.maxPlayers = maxPlayers;
        this.lobbyname = lobbyname;
        this.lobbyID = lobbyID;
        this.mapID = mapID;
        playerIDs = new ArrayList<>();
        vehicleTypes = new ArrayList<>();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getLobbyID() {
        return lobbyID;
    }

    public String getMapID() {
        return mapID;
    }

    public String getLobbyname() {
        return lobbyname;
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }

    public ArrayList<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setPlayerIDs(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public void setVehicleTypes(ArrayList<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    @Override
    public String toString() {
        return lobbyname + " | Players: "+ playerIDs.size() + "/" + maxPlayers;
    }
}
