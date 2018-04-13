package com.panic.tdt4240.connection;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * Created by Eirik on 09-Apr-18.
 */

public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
    }

    /**
     * The method to create a new lobby. Should it return the Lobby? Probably.
     * @param maxPlayerCount The maximum amount of players in the game. Should default to 4?
     * @param mapID The id of the map chosen.
     * @param name A chosen name for the lobby. (Optional?)
     * @return Returns the newly created Lobby.
     */
    public Lobby createLobby(int maxPlayerCount, @NotNull String mapID, String name){

        //TODO: Create and return the Lobby with the designated parameters, and the creator already added.

        return null;

    }

    /**
     * Get all the Lobbies available
     * @return Returns an ArrayList of all available Lobbies.
     */
    public ArrayList<Lobby> getAllLobbies(){
        //TODO: Return a list of all available lobbies - instantiated as Lobby objects.
        return null;
    }

    /**
     * Connect to the given Lobby if this returns true.
     * @param lobbyID The ID of the Lobby you wish to connect to
     * @return Returns true if added on the server, false if it was non-successful.
     */
    public boolean connectToLobby(int lobbyID){

        //TODO: Try to connect to the given Lobby. Should return true if it was successful.

        return false;
    }


    /**
     * A method call to get all the Vehicles for this game. Should be instatiated copies, with ID.
     *
     * @return Returns all the Vehicles for this game.
     */
    public ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> result = null;

        return result;
    }

    /**
     * Get the ID of the current players vehicle.
     *
     * @return returns the ID.
     */
    public String getMyVehicle() {
        return null;
    }


    /**
     * Get the map ID for this game
     *
     * @return Returns the map ID.
     */
    public String getMapID() {
        return null;
    }

    /**
     * reads the history of the game. If the game has no history, the method returns null. The history string needs to be formatted as "CARDID&SENDERID&TARGETID&SEED//" where turns get separated
     * with "ENDTURN//".
     *
     * @param turns The turns String
     * @return An arrayList containing ArrayLists of CardIDs, SenderIDs, TargetIDs and Seeds
     */
    public ArrayList<ArrayList<String[]>> readTurns(String turns){
        if (turns.equals("")) {
            return null;
        }

        String[] data = turns.split("//");
        ArrayList<ArrayList<String[]>> result = new ArrayList<>();
        ArrayList<String[]> currentTurn = new ArrayList<>();
        for (String string : data){
            if(string.equals("ENDTURN")){
                result.add(currentTurn);
                currentTurn = new ArrayList<>();
            } else {
                String[] elements = string.split("&");
                if (elements.length != 4) {
                    throw new IllegalArgumentException("String is not formatted correctly");
                }
                currentTurn.add(new String[]{elements[0],elements[2],elements[1],elements[3]});
            }
        }

        return result;
    }


    /**
     * Generates a String to be sent to server containing cardID, senderID, targetID and priority. The String will be formatted such that the Server can process it.
     * @param moves An arrayList of the moves to be executed
     * @return The formatted string
     */
    public String createCardString(ArrayList<String[]> moves) {
        String returnString = "";
        ModelHolder mh = ModelHolder.getInstance();
        for (String[] move : moves) {
            Card card = mh.getCardById(move[0]);
            String priority = Integer.toString(card.getPriority());
            returnString = returnString + move[0] + "&" + move[2] + "&" + move[1] + "&" + priority + "//";
        }
        return returnString;
    }

    //TODO
    public ArrayList<ArrayList<String[]>> getTurns() {
        return null;
    }
}

