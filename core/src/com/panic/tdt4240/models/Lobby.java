package com.panic.tdt4240.models;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * An object to hold lobby info.
 */

public class Lobby {

    private int maxPlayers = 4;
    private String lobbyname;
    private int lobbyID;
    private String mapID;
    private ArrayList<Integer> playerIDs; //ConnectionIDs?

    public Lobby(int maxPlayers, String lobbyname, int lobbyID, @NotNull String mapID) {
        this.maxPlayers = maxPlayers;
        this.lobbyname = lobbyname;
        this.lobbyID = lobbyID;
        this.mapID = mapID;
        playerIDs = new ArrayList<>();
    }

    public void addPlayer(int ID){
        if (playerIDs.size()<maxPlayers && !playerIDs.contains(ID)){
            playerIDs.add(ID);
        }
        else{
            //TODO: Player could not be added - throw error?
        }
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

    @Override
    public String toString() {
        return lobbyname + " | Players: "+ playerIDs.size() + "/" + maxPlayers;
    }
}
