package com.panic.tdt4240.states;


import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.GameListView;

import java.util.ArrayList;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    private GameListView view;
    ArrayList<String[]> lobbyListData;
    private static String err_full_lobby = "Error: full lobby.";
    private static String err_lobby404 = "Error: lobby not found.";

    public GameListState(GameStateManager gsm){
        super(gsm);
        Connection.getInstance().getAllLobbies();
        lobbyListData = new ArrayList<>();

        // load available games from master server - can be done with updateLobbyList
        /** TESTING {LobbyName, playerCount, maxPlayers, lobbyID} */
        String[] data = {"testing", "1", "4", "0"};
        lobbyListData.add(data);
        Connection.getInstance().getAllLobbies();
/*        Lobby l1 = new Lobby(2, "ENGLISH", 0, "TEST");
        Lobby l2 = new Lobby(3, "MOTHERFUCKER", 1, "TEST");
        Lobby l3 = new Lobby(4, "DO YOU SPEAK IT?!", 2, "TEST");
/**/
        view = new GameListView(this);
    }

    public ArrayList<String[]> getLobbies(){return lobbyListData;}

    @Override
    protected void setUpAdapter() {

    }

    private void updateLobbyList(){
        Connection.getInstance().getAllLobbies();
        //TODO: Actually visualize this list. Use ScrollPane
        // import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
        // https://stackoverflow.com/questions/15484077/libgdx-and-scrollpane-with-multiple-widgets
    }

    /** @param o: what to do, what to do...
     * */
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
                    view.popup(GameListView.error0);
                }
                //TODO: connect with actual Lobby objects instead - use
                else if (o=="error: Missing lobby"){
                    view.popup(GameListView.error1);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void update(float dt) { }

    @Override
    public void render() { view.render(); }

    @Override
    public void dispose() { view.dispose(); }

    /**
     GET_LOBBIES:LobbyName1,CurrentPlayerNum1,MaxPlayers1,ID1&LobbyName2,CurrentPlayerNum2,...,MaxPlayerNumN, IDN
     ':'    - remove
     '&'    - separate lobbies
     ','    - lobby data separators
     Result: String[], {LobbyName, playerCount, maxPlayers, lobbyID}

     @return 1. Data is added to the arrayList in the format:
         { LobbyName \t   playerCount/maxPlayers, lobbyID}
     */
    public boolean readLobbyData(String s){
        String[] untagged = s.split(":"); // removes the first separator
        String[] full_list = untagged[1].split("&");
        for (String lobby : full_list){
            String[] lobby_data = lobby.split(",");
            String[] cleaned_data = {lobby_data[0] + "\tSlots: " + lobby_data[1]
                    + "/" + lobby_data[2], lobby_data[3]};
            lobbyListData.add(cleaned_data);
        }
        return full_list.length == lobbyListData.size() ; // simple validity check
    }


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
