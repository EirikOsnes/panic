package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.Connection;
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
        ArrayList<Vehicle> vehicles = connection.getAllVehicles();
        String myVehicleID = connection.getMyVehicle();
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
        String mapID = connection.getMapID();
        Map myMap = ModelHolder.getInstance().getMapById(mapID);
        gi.setMap(myMap);
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
}
