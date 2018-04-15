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

    ArrayList<String[]> lobbyListData;
    private static String err_full_lobby = "Error: full lobby.";
    private static String err_lobby404 = "Error: lobby not found.";

    public GameListState(GameStateManager gsm){
        super(gsm);
        lobbyListData = new ArrayList<>();
        view = new GameListView(this);
        updateLobbyList();
        // load available games from master server - can be done with updateLobbyList
        // ... maybe with ping?

    }

    private void updateLobbyList(){
        Connection.getInstance().getAllLobbies();
        //TODO: Actually visualize this list. Use ScrollPane
        // import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
        // https://stackoverflow.com/questions/15484077/libgdx-and-scrollpane-with-multiple-widgets
    }

    public ArrayList<String[]> getLobbies() {
        return lobbyListData;
    }

    @Override
    public void handleInput(Object o) {
        // when a lobby is clicked, enter it.
        if (o.getClass()==Integer.class){
            if ((int) o == -1) gsm.reset();  // exit to main menu
        }
        if ( o.getClass() == String.class){  // lobbyID is still a string
            if ((int) o >= 0){ // lobbyID entered
                Connection.getInstance().connectToLobby((int) o);
            }
            try{    // error handling
                if (o=="error:Full lobby"){
                    ((GameListView)view).popup(GameListView.error0);
                }
                //TODO: connect with actual Lobby objects instead - use
                else if (o=="error: Missing lobby"){
                    ((GameListView)view).popup(GameListView.error1);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void update(float dt) { }

    @Override
    public void render() { ((GameListView)view).render(); }

    @Override
    public void dispose() { ((GameListView)view).dispose(); }

    /**
     LobbyName1,CurrentPlayerNum1,MaxPlayers1,ID1&LobbyName2,CurrentPlayerNum2,...,MaxPlayerNumN, IDN
     '&'    - separate lobbies
     ','    - lobby data separators
     Result: String[], {LobbyName, playerCount, maxPlayers, lobbyID}

     @return 1. Data is added to the arrayList in the format:
         { LobbyName \t   playerCount/maxPlayers, lobbyID}
     */
    public boolean readLobbyData(String s){
        String[] full_list = s.split("&");
        for (String lobby : full_list){
            String[] lobby_data = lobby.split(",");
            String[] cleaned_data = {lobby_data[0] + "\tSlots: " + lobby_data[1]
                    + "/" + lobby_data[2], lobby_data[3]};
            lobbyListData.add(cleaned_data);
        }
        return full_list.length == lobbyListData.size() ; // simple validity check
    }


    private void connectToLobby(int lobbyID){
        Connection.getInstance().connectToLobby(lobbyID);
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new GameListAdapter();
    }

    private class GameListAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "GET_LOBBIES":
                    readLobbyData(strings[1]);
                    break;
                case "LOBBY_SUCCESSFUL":
                    gsm.push(new GameLobbyState(gsm,Integer.parseInt(strings[1])));
                    break;
                case "LOBBY_FAILED":
                    //TODO: Pop-up error? Lobby was full or deleted.
                    updateLobbyList();
                    break;
            }
        }

    }

}
