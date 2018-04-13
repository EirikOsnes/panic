package com.panic.tdt4240.states;


import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.GameListView;

import java.util.ArrayList;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    GameListView view;
    ArrayList<Lobby> lobbies;

    public GameListState(GameStateManager gsm){
        super(gsm);
        view = new GameListView(this);
        lobbies = new ArrayList<>();
        // load available games from master server - can be done with updateLobbyList
        // ... maybe with ping?

    }

    private void updateLobbyList(){
        lobbies = Connection.getInstance().getAllLobbies();
        //TODO: Actually visualize this list.
    }

    @Override
    public void handleInput(Object o) {
        // when a lobby is clicked, enter it.
        if ( o.getClass() == String.class){
            try{
                //TODO: connect with actual Lobby objects instead - use

            } catch(Exception e){

            }
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

    }

    /**
     * Connect to the lobby designated - if allowed
     * @param lobby The lobby you wish to connect to
     */
    private void connectToLobby(Lobby lobby){
        if(Connection.getInstance().connectToLobby(lobby.getLobbyID())){
            gsm.push(new GameLobbyState(gsm,lobby));
        }
        else{
            //TODO: Cannot join the lobby - it might be full. Maybe give a error pop-up, and refresh the lobby list with updateLobbyList()?
        }
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new GameListAdapter();
    }

    private class GameListAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {

        }
    }

}
