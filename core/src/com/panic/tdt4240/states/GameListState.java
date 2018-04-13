package com.panic.tdt4240.states;


import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.GameListView;

import java.util.ArrayList;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    GameListView view;
    ArrayList<Lobby> lobbies;
    private static String err_full_lobby = "Error: full lobby.";
    private static String err_lobby404 = "Error: lobby not found.";

    public GameListState(GameStateManager gsm){
        super(gsm);
        view = new GameListView(this);
        lobbies = new ArrayList<>();
        // load available games from master server - can be done with updateLobbyList
        // ... maybe with ping?

    }

    public ArrayList<Lobby> getLobbies(){return lobbies;}

    private void updateLobbyList(){
        lobbies = Connection.getInstance().getAllLobbies();
        //TODO: Actually visualize this list. Use ScrollPane
        // import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
        // https://stackoverflow.com/questions/15484077/libgdx-and-scrollpane-with-multiple-widgets
    }

    @Override
    public void handleInput(Object o) {
        // when a lobby is clicked, enter it.
        if (o == 1){
            view.dispose();
            gsm.pop();
        }
        if ( o.getClass() == String.class){
            try{
                if (o=="error:Full lobby"){
                    view.popup((String) o);
                }
                //TODO: connect with actual Lobby objects instead - use
                else{
                    connectToLobby((Lobby) o);
                }
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
            if (lobby.getMaxPlayers() == lobby.getPlayerIDs().size()) {
                handleInput("Error: full lobby");
                updateLobbyList();
            }
        }
    }

}
