package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.GameLobbyView;

import java.lang.reflect.Array;
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
        this.lobby = lobby;
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
        lobby = Connection.getInstance().updateLobby(lobby.getLobbyID());
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
        view.dispose();
    }

}
