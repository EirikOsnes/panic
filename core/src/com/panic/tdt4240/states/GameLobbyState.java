package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.GameLobbyView;

import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyState extends State {

    // fields: player(s), numOfPlayers...
    GameLobbyView view;
    Lobby lobby;

    public GameLobbyState(GameStateManager gsm, Lobby lobby){
        super(gsm);
        System.out.println("GameLobbyState");
        this.lobby = lobby;
        Connection.getInstance().updateLobby(lobby.getLobbyID());
        view = new GameLobbyView(this);

        // generate lobbyID from somewhere...
    }

    public void launchGame(){
        // might want to test this

        // needs additional parameters?
        //gsm.push(new PlayCardState(gsm));
    }

    /**
     * Update the lobby - should be called every second maybe?
     */
    private void updateLobby(){
        Connection.getInstance().updateLobby(lobby.getLobbyID());
    }

    /**
     * Use this method to change/choose vehicle type (i.e. EDDISON)
     * @param vehicleType The vehicle type.
     */
    private void setVehicleType(String vehicleType){
        Connection.getInstance().chooseVehicleType(vehicleType,lobby.getLobbyID());
    }

    /**
     * Set me to ready.
     */
    private void setReady(){
        Connection.getInstance().setReady(lobby.getLobbyID());
    }

    /**
     * Leave the Lobby
     */
    private void leaveLobby(){
        Connection.getInstance().leaveLobby(lobby.getLobbyID());
        gsm.pop();
    }

    @Override
    public void handleInput(Object o) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) gsm.reset();

    }

    @Override
    public void update(float dt) {
        // players joining...?
        // Call updateLobby() when applicable.

        //Is all players ready? lobby.isAllReady() == true
        //If so, should start a count-down for game starting?
    }

    @Override
    public void render() {
        view.render(); }

    @Override
    public void dispose() {

    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new GameLobbyAdapter();
    }

    private class GameLobbyAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "LOBBY_INFO":
                    parseLobby(strings);
                    break;
                case "GAME_START":
                    //TODO: Create this call, and handle it pls.
                    break;

            }


        }

        private void parseLobby(String[] strings){
            Lobby myLobby = new Lobby(Integer.parseInt(strings[1]),strings[2],Integer.parseInt(strings[3]),strings[4]);
            String[] playerIDstrings = strings[5].split("&");
            String[] vehicleTypestrings = strings[6].split("&");
            ArrayList<Integer> playerIDs = new ArrayList<>();
            ArrayList<String> vehicleTypes = new ArrayList<>();
            for (int i = 0; i < playerIDstrings.length; i++) { //Assuming proper set up here - the same amount of values.
                playerIDs.add(Integer.parseInt(playerIDstrings[i]));
                vehicleTypes.add((vehicleTypestrings[i].equals("NONE")) ? null : vehicleTypestrings[i]);
            }
            myLobby.setPlayerIDs(playerIDs);
            myLobby.setVehicleTypes(vehicleTypes);
            lobby = myLobby;
        }
    }

}
