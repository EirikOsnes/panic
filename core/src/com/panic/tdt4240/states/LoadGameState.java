package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.XMLParser;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The state for loading up a new game - or reloading a game.
 */

public class LoadGameState extends State {

    private GameInstance gi;
    private Connection connection;
    private boolean isLoading; //Flag to use for rendering of a loading screen.

    protected LoadGameState(GameStateManager gsm) {
        super(gsm);
        gi = GameInstance.getInstance();
        connection = Connection.getInstance();
        setUpGameInstance();
        checkForHistory();
    }

    /**
     * Resets the GameInstance singleton and sets it up anew with the needed information.
     */
    private void setUpGameInstance(){
        gi.reset();
        isLoading = true;
        connection.getGameInfo();
    }

    private void setGIValues(ArrayList<Vehicle> vehicles, String mapID, String myVehicleID){
        Vehicle myVehicle = null;
        for (Vehicle v : vehicles) {
            if (v.getVehicleID().equals(myVehicleID))
                myVehicle = v;
        }
        gi.setVehicles(vehicles);
        XMLParser parser = new XMLParser();
        Stack<Card> myCards = parser.parseCardStack(myVehicle.getVehicleType());
        gi.setPlayer(new Player(myCards));
        gi.getPlayer().setVehicle(myVehicle);
        Map myMap = ModelHolder.getInstance().getMapById(mapID);
        gi.setMap(myMap);
        isLoading = false;
    }


    /**
     * Checks to see if the client is reconnecting - i.e. there is a history of cards played, and plays these if that is the case.
     */
    private void checkForHistory() {
        ArrayList<ArrayList<String[]>> log = connection.getTurns();

        if(log!=null){
            for (ArrayList<String[]> turn : log) {
                gi.playTurn(turn);
            }
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public void handleInput(Object o) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new LoadGameAdapter();
    }

    private class LoadGameAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "GAME_INFO":
                    parseGameInfo(strings);
                    break;
            }

        }

        private void parseGameInfo(String[] strings){
            String[] vehicleStrings = strings[1].split("&");
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            for (String vehicleString : vehicleStrings) {
                String[] vehicleInfo = vehicleString.split(",");
                Vehicle myVehicle = ModelHolder.getInstance().getVehicleByName(vehicleInfo[0]).cloneVehicleWithId(vehicleInfo[1]);
                //TODO: myVehicle.setColor(vehicleInfo[2]);
                vehicles.add(myVehicle);
            }

            setGIValues(vehicles, strings[2], strings[3]);

        }
    }
}
