package com.panic.tdt4240.models;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * An object to hold lobby info.
 */

public class Lobby {

    int maxPlayers = 4;
    String lobbyname;
    int lobbyID;
    String mapID;
    ArrayList<Integer> playerIDs;

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
}
