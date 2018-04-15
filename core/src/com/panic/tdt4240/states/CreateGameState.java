package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.CreateGameView;

import java.util.ArrayList;

/**
 * Created by magnus on 12.03.2018.
 */

public class CreateGameState extends State {

    CreateGameView view;
    Connection connection;
    int maxPlayerCount;
    String mapID;
    String name;

    public CreateGameState(GameStateManager gsm){
        super(gsm);
        connection = Connection.getInstance();
        view = new CreateGameView(this);
    }

    /**
     * Method to run the onClick for the create click
     */
    private void createButtonClick(){
        //TODO: Actually set the maxPlayerCount, mapID and name parameters.
        connection.createLobby(maxPlayerCount,mapID,name);
    }


    @Override
    public void handleInput(Object o) {

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
    protected void setUpAdapter() {
        callbackAdapter = new CreateGameAdapter();
    }

    private class CreateGameAdapter implements ICallbackAdapter{

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "LOBBY_INFO":
                    parseLobby(strings);
                    break;
            }


        }

        private void parseLobby(String[] strings){
            Lobby myLobby = new Lobby(Integer.parseInt(strings[1]),strings[2],Integer.parseInt(strings[3]),strings[4]);
            System.out.println("Lobby parsed: " + myLobby.toString());
            gsm.push(new GameLobbyState(gsm,myLobby));
        }
    }
}
