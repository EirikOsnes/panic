package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.GameLobbyView;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyState extends State {


    GameLobbyView view;
    Lobby lobby;

    public GameLobbyState(GameStateManager gsm, int lobbyID){
        super(gsm);
        Connection.getInstance().updateLobby(lobbyID);
        view = new GameLobbyView(this);
    }

    public Lobby getLobby(){return lobby;}

    public void launchGame(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                gsm.push(new LoadGameState(gsm, lobby.getLobbyID()));
            }
        });
    }

    /**
     * Update the lobby - should be called every second maybe?
     */
    private void updateLobby(){
        Connection.getInstance().updateLobby(lobby.getLobbyID());
        view.updateView();
    }

    /**
     * Use this method to change/choose vehicle type (i.e. EDDISON)
     * @param vehicleType The vehicle type.
     */
    private void setVehicleType(String vehicleType){
        Connection.getInstance().chooseVehicleType(vehicleType,lobby.getLobbyID());
    }

    /**
     * Set me to ready.
     */
    public void setReady(){
        Connection.getInstance().chooseVehicleType("EDDISON",lobby.getLobbyID());
    }

    /**
     * Leave the Lobby
     */
    private void leaveLobby(){
        Connection.getInstance().leaveLobby(lobby.getLobbyID());
        gsm.pop();
    }

    @Override
    public void handleInput(Object o) {
        String s = (String) o;
        if (s.equals("-1")){
            gsm.reset();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) gsm.reset();

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

    private class GameLobbyAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "LOBBY_INFO":
                    parseLobby(strings);
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
            String[] playerIDstrings = strings[5].split("&");
            String[] vehicleTypestrings = strings[6].split("&");
            ArrayList<Integer> playerIDs = new ArrayList<>();
            ArrayList<String> vehicleTypes = new ArrayList<>();
            for (int i = 0; i < playerIDstrings.length; i++) { //Assuming proper set up here - the same amount of values.
                playerIDs.add(Integer.parseInt(playerIDstrings[i]));
                vehicleTypes.add((vehicleTypestrings[i].equals("NONE")) ? null : vehicleTypestrings[i]);
            }
            myLobby.setPlayerIDs(playerIDs);
            myLobby.setVehicleTypes(vehicleTypes);
            System.out.println(myLobby.toString());
            lobby = myLobby;
        }
    }

}
