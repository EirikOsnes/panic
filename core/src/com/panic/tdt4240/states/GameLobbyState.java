package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    @Override
    public void handleInput(Object o) {
    }

    @Override
    public void update(float dt) {
        // players joining...?
    }

    @Override
    public void render() {
        view.render(); }

    @Override
    public void dispose() {

    }

}
