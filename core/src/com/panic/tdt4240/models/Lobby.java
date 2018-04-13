package com.panic.tdt4240.models;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * An object to hold lobby info. Values should only be changed by Connection.
 */

public class Lobby {

    private int maxPlayers = 4;
    private String lobbyname;
    private int lobbyID;
    private String mapID;
    private ArrayList<Integer> playerIDs; //ConnectionIDs?
    private ArrayList<String> vehicleTypes;
    private ArrayList<Boolean> playersReady;

    public Lobby(int maxPlayers, String lobbyname, int lobbyID, @NotNull String mapID) {
        this.maxPlayers = maxPlayers;
        this.lobbyname = lobbyname;
        this.lobbyID = lobbyID;
        this.mapID = mapID;
        playerIDs = new ArrayList<>();
        vehicleTypes = new ArrayList<>();
        playersReady = new ArrayList<>();
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

    public ArrayList<Boolean> getPlayersReady() {
        return playersReady;
    }

    public void setPlayerIDs(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public void setVehicleTypes(ArrayList<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public void setPlayersReady(ArrayList<Boolean> playersReady) {
        this.playersReady = playersReady;
    }

    /**
     * Returns true if all players are ready.
     * @return Returns true if all players are ready, false otherwise.
     */
    public boolean isAllReady(){
        for (Boolean b : playersReady) {
            if (!b){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return lobbyname + " | Players: "+ playerIDs.size() + "/" + maxPlayers;
    }
}
