package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.CreateGameView;
import java.lang.Thread;

/**
 * Created by magnus on 12.03.2018.
 */

public class CreateGameState extends State {

    CreateGameView view;
    int maxPlayerCount;
    String mapID;
    String name;


    public CreateGameState(GameStateManager gsm){
        super(gsm);
        view = new CreateGameView(this);
        System.out.println("Thread check 2: " + Thread.currentThread().toString());
    }

    /**
     * Method to run the onClick for the create click
     */

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void createButtonClick(){
        //TODO: Actually set the maxPlayerCount, mapID and name parameters.
        Connection.getInstance().createLobby(maxPlayerCount,mapID,name);
    }
    public String[] getMapIDs(){
        return ModelHolder.getInstance().getMapIDs();
    }
    public String[] getMaxPlayers(){
        String[] max_players = new String[GlobalConstants.MAX_PLAYERS-1];
        for (Integer i = 0; i < GlobalConstants.MAX_PLAYERS-1; i++) {
            max_players[i] = String.valueOf(i + 2);
        }
        return max_players;
    }

    @Override
    public void handleInput(Object o) {
        String s = (String) o;
        if (s.equals("-1")){
            gsm.reset();
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    @Override
    public AbstractView getView() {
        return view;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new CreateGameAdapter();
    }

    private class CreateGameAdapter implements ICallbackAdapter{

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            // I mean, should probably use {}-clauses rather than
            // shitty breaks...
            switch (strings[0]) {
                case "LOBBY_INFO":
                    parseLobby(strings);
                    // ENTERS LOBBY STATE
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
            }
        }

        private void parseLobby(String[] strings){
            Lobby myLobby = new Lobby(Integer.parseInt(strings[1]),strings[2],Integer.parseInt(strings[3]),strings[4]);
            System.out.println("Lobby parsed:: " + myLobby.toString());
            final int ID = myLobby.getLobbyID();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    gsm.push(new GameLobbyState(gsm,ID));
                }
            });

        }

        private void parseLobby(String[] strings){
            Lobby myLobby = new Lobby(Integer.parseInt(strings[1]),strings[2],Integer.parseInt(strings[3]),strings[4]);
            System.out.println("Lobby parsed: " + myLobby.toString());
            final int ID = myLobby.getLobbyID();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    gsm.set(new GameLobbyState(gsm,ID));
                }
            });

        }

    }
}
