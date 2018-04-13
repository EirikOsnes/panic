package com.panic.tdt4240.connection;

import android.support.annotation.NonNull;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The class to communicate with the server
 */

public class Connection extends WebSocketClient{

    private static Connection ourInstance;
    private ICallbackAdapter adapter;

    public static Connection getInstance() {
        if(ourInstance == null){
            try {
                URI uri = new URI("ws://panicserver.herokuapp.com");
                ourInstance = new Connection(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return ourInstance;
    }

    private Connection(URI uri) {
        super(uri);
    }

    /**
     * The method to create a new lobby. Should it return the Lobby? Probably.
     * @param maxPlayerCount The maximum amount of players in the game. Should default to 4?
     * @param mapID The id of the map chosen.
     * @param name A chosen name for the lobby. (Optional?)
     * @return Returns the newly created Lobby.
     */
    public Lobby createLobby(int maxPlayerCount, @NonNull String mapID, String name){

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
     * Remove the player from the given Lobby.
     * @param lobbyID The ID of the Lobby
     */
    public void leaveLobby(int lobbyID){

        //TODO: Remove me from the lobby.

    }

    /**
     * Get the latest state of the given Lobby - used to update the GameLobbyState
     * @param lobbyID The id of the lobby
     * @return Returns the updated Lobby object.
     */
    public Lobby updateLobby(int lobbyID){

        //TODO: Return the latest state of this Lobby.

        return null;
    }

    /**
     * Set the vehicle type for the given Lobby to the given vehicleType.
     * @param vehicleType
     * @param lobbyID
     */
    public void chooseVehicleType(String vehicleType, int lobbyID){

        //TODO: Set my vehicle in the Lobby to tbe given vehicle type

    }

    /**
     * Set me to ready for the given lobby
     * @param lobbyID The ID of the lobby.
     */
    public void setReady(int lobbyID){

        //TODO: Set me to ready

    }

    /**
     * A method call to get all the Vehicles for this game. Should be instatiated copies, with ID.
     *
     * @return Returns all the Vehicles for this game.
     */
    public ArrayList<Vehicle> getAllVehicles() {

        //TODO: Actually receive and parse the vehicles. Comments below is one way:
        //Probably get strings on the form {VehicleType, VehicleID}
        //Foreach string: result.add(ModelHolder.getInstance.getVehicleByName(VehicleType).clone(VehicleID))

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
     * Tell the server that runEffectsState is done animating, so the next turn can begin.
     */
    public void sendDoneAnimating(){

        //TODO: Actually send this info to the server.

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

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message){
        if (adapter != null) {
            adapter.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

    public void setAdapter(ICallbackAdapter adapter) {
        this.adapter = adapter;
    }
}

