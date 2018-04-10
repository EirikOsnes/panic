package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    int lobbyID;
    ArrayList<Player> players;

    public GameLobbyState(GameStateManager gsm, String lobbyID){
        super(gsm);
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

    public ArrayList<Player> getPlayers(){return players;}

    public void playerJoined(Player p){
        players.add(p);
        view.playerJoined(p);
    }

    public Player getNewestPlayer(){return players.get(players.size()-1);}

}
