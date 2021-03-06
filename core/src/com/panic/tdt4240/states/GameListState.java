package com.panic.tdt4240.states;


import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.GameListView;
import java.lang.Thread;

import java.util.ArrayList;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    ArrayList<String[]> lobbyListData;
    private static String err_full_lobby = "Error: full lobby.";
    private static String err_lobby404 = "Error: lobby not found.";
    boolean triedToEnterLobby;
    boolean failedToEnterLobby;

    public GameListState(GameStateManager gsm){
        super(gsm);
        triedToEnterLobby = false; failedToEnterLobby = false;
        lobbyListData = new ArrayList<>();
        view = new GameListView(this);
        updateLobbyList();
        // load available games from master server - can be done with updateLobbyList
    }

    public ArrayList<String[]> getLobbyListData(){return lobbyListData;}


    public void updateLobbyList(){
        Connection.getInstance().getAllLobbies();
        //TODO: Actually visualize this list. Use ScrollPane
        // import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
        // https://stackoverflow.com/questions/15484077/libgdx-and-scrollpane-with-multiple-widgets
    }
    
    public void connectToLobby(int i){
        Connection.getInstance().connectToLobby(i);
    }

    @Override
    public void handleInput(Object o) {
        final String s = (String) o;
        // when a lobby is clicked, enter it.
        if (s.equals("-1")) gsm.reset();  // exit to main menu
        else {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    connectToLobby(Integer.parseInt(s)); // ACTUAL CODE... i think.
                    System.out.println("New lobby state SHOULD BE made");
                    updateLobbyList();
                }
            });
/*            if (failedToEnterLobby) {
                updateLobbyList();
                System.out.println("Check 2; if this ever triggers then you fucked up bigtime");
            } else if (triedToEnterLobby) {
                failedToEnterLobby = true;
                System.out.println("Check 1");
                updateLobbyList();
            } // nothing has gone wrong*/
        }
        try {    // error handling
            if (s.equals("error:Full lobby")) {
                ((GameListView)view).popup(GameListView.error0);
            }
            //
            else if (s.equals("error: Missing lobby")) {
                ((GameListView)view).popup(GameListView.error1);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void update(float dt) { }

    @Override
    public void render() { view.render(); }

    @Override
    public void dispose() { view.dispose(); }

    @Override
    public AbstractView getView() {
        return view;
    }

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
        lobbyListData = new ArrayList<>();
        String[] full_list = s.split("&");
        for (String lobby : full_list){
            String[] lobby_data = lobby.split(",");
            String[] cleaned_data = {Connection.getInstance().parseFromServer(lobby_data[0]) + "  Slots:  " + lobby_data[1]
                    + "/" + lobby_data[2], lobby_data[3]};
//            System.out.println(cleaned_data[0] + "\t::\t" +cleaned_data[1]);
            lobbyListData.add(cleaned_data);
        }
//        System.out.println("done");
        return full_list.length == lobbyListData.size() ; // simple validity check
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
                    if(strings.length>1) {
                        readLobbyData(strings[1]);
                        ((GameListView)view).updateView();
                    }
                    else {
                        lobbyListData = new ArrayList<>();
                        ((GameListView)view).updateView();
                    }

                    break;
                    // CLICKED ON A LOBBY, RECEIVING LOBBY DATA SOON
                case "LOBBY_SUCCESSFUL":
                    System.out.println("Thread check 1: " + Thread.activeCount());
                    final String s = strings[1];
/*                    gsm.push(new GameLobbyState(gsm,Integer.parseInt(s))); /**/

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Thread check postRunnable in listState onMessage: " +
                                Thread.currentThread().toString());
                            gsm.push(new GameLobbyState(gsm,Integer.parseInt(s))); /**/
                        }
                    }); /**/
                    break;

                case "LOBBY_FAILED":
                    //TODO: Pop-up error? Lobby was full or deleted.
                    updateLobbyList();
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
            }
        }
    }
}
