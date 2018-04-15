package com.panic.tdt4240.connection;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
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
    private int connectionID = 0;

    public static Connection getInstance() {
        if(ourInstance == null){
            try {
                URI uri = new URI("ws://panicserver.herokuapp.com");
                ourInstance = new Connection(uri);
                ourInstance.connectBlocking(); //FIXME: This returns a boolean - should it be used?
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!ourInstance.getSocket().isConnected()){
            try {
                //ourInstance.connectBlocking();
                ourInstance.reconnectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ourInstance;
    }

    private Connection(URI uri) {
        super(uri);
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void test(){
        ourInstance.send("TEST");
    }

    //Get a personal connectionID from the server
    public void findConnectionID(){
        this.send("CONNECTION_ID");
    }

    /**
     * The method to create a new lobby. Should it return the Lobby? Probably.
     * @param maxPlayerCount The maximum amount of players in the game. Should default to 4?
     * @param mapID The id of the map chosen.
     * @param name A chosen name for the lobby. (Optional?)
     * Return the new Lobby as a string on the form:
     * CREATE_LOBBY:MaxPlayers:LobbyName:LobbyID:MapID
     */
    public void createLobby(int maxPlayerCount, @NonNull String mapID, String name){

        String message = "CREATE//" + mapID + "//" + maxPlayerCount + "//" + parseToServer(name);
        this.send(message);

    }

    private String parseToServer(String string){
        return string.replaceAll("," , "§" ).replaceAll(":", "€").replaceAll("//", "£").replaceAll("&", "¤");
    }

    public String parseFromServer(String string){
        return string.replaceAll("§", ",").replaceAll("€", ":").replaceAll("£", "//").replaceAll("¤","&");
    }

    /**
     * Get all the Lobbies available
     * Returns an ArrayList of all available Lobbies as a string on the form:

     * GET_LOBBIES: {4 fields} & {4 fields} & ... repeating. '&' = lobby separator

     * GET_LOBBIES:ID1,Lobbyname1,CurrentPlayerNum1,MaxPlayers1&ID2,LobbyName2,CurrentPlayerNum2,...,MaxPlayerNumN

     */
    public void getAllLobbies(){
        this.send("GET_LOBBIES");
    }

    /**
     * Connect to the given Lobby if it is possible.
     * @param lobbyID The ID of the Lobby you wish to connect to
     */
    public void connectToLobby(int lobbyID){
        this.send("ENTER//"+lobbyID);
    }


    /**
     * Remove the player from the given Lobby.
     * @param lobbyID The ID of the Lobby
     */
    public void leaveLobby(int lobbyID){

        this.send("TOGAME//" + lobbyID + "//LEAVE_GAME");

    }

    /**
     * Get the latest state of the given Lobby - used to update the GameLobbyState
     * @param lobbyID The id of the lobby
     * Return the updated Lobby as a string on the form:
     * CREATE_LOBBY:MaxPlayers:LobbyName:LobbyID:MapID:PlayerID1&PlayerID2&...&PlayerIDN:VehicleType1&VehicleType2&...&VehicleTypeN
     *
     */
    public void updateLobby(int lobbyID){

        this.send("TOGAME//"+lobbyID+"//GET_LOBBY_INFO");

    }

    /**
     * Set the vehicle type for the given Lobby to the given vehicleType.
     * @param vehicleType
     * @param lobbyID
     */
    public void chooseVehicleType(String vehicleType, int lobbyID){
        this.send("TOGAME//" + lobbyID + "//VEHICLE_SET//" + vehicleType + "//" + connectionID);
    }

    /**
     * Set me to ready for the given lobby
     * @param lobbyID The ID of the lobby.
     */
    public void setReady(int lobbyID){

        //TODO: Set me to ready
        //FOR NOW THIS IS NOT TO BE USED

    }

    /**
     * Return on the form GAME_INFO:VehicleType1,VehicleID1,Color1&VehicleType2,...ColorN:MapID:MyVehicleID
     */
    public void getGameInfo(){
        this.send("GAME_INFO");
    }

    public void sendPlayCardState(){
        this.send("BEGIN_TURN");
    }

    /**
     * Tell the server that you have changed to the RunEffectsState, and thus are ready to receive cards.
     */
    public void sendRunEffectsState(){
        this.send("ENTERED_RUN_EFFECT_STATE");
    }

    /**
     * The history string needs to be formatted as "CARDID&SENDERID&TARGETID&SEED//" where turns get separated
     * with "ENDTURN//".
     */
    public void getLog(){
        //TODO: send a getAllTurns command
        //FOR NOW THIS IS NOT TO BE USED
    }



    /**
     * Generates a String to be sent to server containing cardID, senderID, targetID and priority. The String will be formatted such that the Server can process it.
     * @param moves An arrayList of the moves to be executed
     * Sends a round of cards on the format SEND_CARDS//CardID1&SenderID1&TargetID1&Priority1//...&TargetIDN&PriorityN//
     */
    public void sendTurn(ArrayList<String[]> moves) {
        String returnString = "SEND_CARDS//";
        ModelHolder mh = ModelHolder.getInstance();
        for (String[] move : moves) {
            Card card = mh.getCardById(move[0]);
            String priority = Integer.toString(card.getPriority());
            returnString = returnString + move[0] + "&" + move[2] + "&" + move[1] + "&" + priority + "//";
        }
        this.send(returnString);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Gdx.app.log("", "onOpen");
        System.out.println("onOpen");
    }

    @Override
    public void onMessage(String message){
        Gdx.app.log("", message);
        System.out.println("RECEIVED MESSAGE: " + message);
        if (adapter != null) {
            adapter.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Gdx.app.log("", "onClose");
        System.out.println("onClose");
    }

    @Override
    public void onError(Exception ex) {
        Gdx.app.log("onError", ex.getMessage());
        System.out.println("onError.....: " + ex.getMessage());
    }

    public void setAdapter(ICallbackAdapter adapter) {
        this.adapter = adapter;
    }
}

