package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.GameLobbyView;

import java.util.ArrayList;

/**
 *
 * Created by victor on 12.03.2018.
 *
 * NO MORE THREAD PROBLEMS AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH
 *
 * Sequence to implement:
 *      1. Select vehicle   -- done
 *      2. Ready up         -- done
 *      3. Indicate the player is waiting -- done
 *
 *  "Problems": if the lobby creator leaves, shit goes real bad.
 **/

public class GameLobbyState extends State {


    GameLobbyView view;
    private Lobby lobby;
    private int lobbyID;
    private boolean dataLoaded;
    int currentPlayerCount;
    private boolean isPlayerReady;

    public GameLobbyState(GameStateManager gsm, int lobbyID){
        super(gsm);
        Connection.getInstance().updateLobby(lobbyID);
        view = new GameLobbyView(this);
        dataLoaded =false;
//        view = new GameLobbyView(this);
        this.lobbyID=lobbyID;
//        Connection.getInstance().updateLobby(lobbyID);
        // updateLobby() cannot run because data has not yet arrived
        updateLobby();
    }

    public Lobby getLobby(){return lobby;}

    public void launchGame(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                gsm.push(new LoadGameState(gsm, lobbyID));
            }
        });
    }

    /**
     * Update the lobby - should be called every second maybe?
     */
    public void updateLobby(){
        Connection.getInstance().updateLobby(this.lobbyID);
        System.out.println("---Messaged received---\n" +
                "Thread check 3: " + Thread.currentThread().toString());

        // DELEGATE VIEW UPDATE
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                System.out.println("Attempting to update view from postRunnable");
                view.updateView();
            }
        }); /**/
    }

    public boolean isPlayerReady(){return isPlayerReady; }

    public void setPlayerReady(boolean r){ isPlayerReady = r; }

    @Override
    public void handleInput(Object o) {
        String s = (String) o;
        if ( s.equals("-2")) Connection.getInstance().chooseVehicleType(view.getSelectedVehicle(), lobbyID);
        else if (s.equals("-1")){
            leaveLobby();
            view.updateView();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) gsm.reset();

    }

    /**
     * [OLD COMMENT] Use this method to change/choose vehicle type (i.e. EDDISON)
     *
     * [New comment]
     * This method is fired when 'Ready' button is pressed.
     * @param vehicleType The vehicle type.
     */
    private void setVehicleType(String vehicleType){
        Connection.getInstance().chooseVehicleType(vehicleType,lobby.getLobbyID());
    }

    /**
     *
     */
    public void setReady(){
        Connection.getInstance().chooseVehicleType("EDDISON",lobby.getLobbyID());
    }

    /**
     * Leave the Lobby
     */
    private void leaveLobby(){
        Connection.getInstance().leaveLobby(lobbyID);
        //        gsm.pop(); // sends you to EITHER lobby list or creategame, but we want the list.
        gsm.set(new GameListState(gsm));
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
        callbackAdapter = new GameLobbyAdapter();
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean b) {
        this.dataLoaded = b;
    }

    public int getLobbyID(){return lobbyID;}


    private class GameLobbyAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");
            switch (strings[0]){
                case "LOBBY_INFO":
                    parseLobby(strings);
                    // View update should trigger whenever server sends lobby info
                    // ... and server should send new info whenever a player joins.
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            view.updateView();
                        }
                    });
                    //view!=null
                    break;
                case "GAME_START":
                    launchGame();
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;

            }

        }

        private void parseLobby(String[] strings){
            Lobby myLobby = new Lobby(Integer.parseInt(strings[1]),Connection.getInstance().parseFromServer(strings[2]),Integer.parseInt(strings[3]),strings[4]);
            System.out.println(strings);
            String[] playerIDstrings = strings[5].split("&");
            String[] vehicleTypestrings = strings[6].split("&");    //
            ArrayList<Integer> playerIDs = new ArrayList<>();
            ArrayList<String> vehicleTypes = new ArrayList<>();
            for (int i = 0; i < playerIDstrings.length; i++) { //Assuming proper set up here - the same amount of values.
/*                System.out.println("Parsing slot: " + i);
                System.out.println(playerIDstrings[i]);
                System.out.println(vehicleTypestrings[i]);/**/
                playerIDs.add(Integer.parseInt(playerIDstrings[i]));

                // TEST CODE
                vehicleTypes.add("EDDISON");

                //PROPER CODE
                vehicleTypes.add((vehicleTypestrings[i].equals("NONE")) ? null : vehicleTypestrings[i]);
            }
            // when players come and go, undo "Ready up" button's functions.
            if (currentPlayerCount != playerIDs.size()){
                setPlayerReady(false);
                runPostRunnable();
            }
            currentPlayerCount = playerIDs.size();
            myLobby.setPlayerIDs(playerIDs);
            myLobby.setVehicleTypes(vehicleTypes);
            lobby = myLobby;
            setDataLoaded(true);
            runPostRunnable();

        }

        public void runPostRunnable(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                view.updateView();
            }
        });
        }


    }

}