package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Deck;
import com.panic.tdt4240.models.GameModelHolder;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Eirik on 07-Apr-18.
 */

public class LoadGameState extends State {

    GameModelHolder gmh;
    Connection connection;

    protected LoadGameState(GameStateManager gsm) {
        super(gsm);
        gmh = GameModelHolder.getInstance();
        connection = Connection.getInstance();

    }

    private void setUpGameModelHolder(){
        gmh.reset();
        ArrayList<Vehicle> vehicles = connection.getAllVehicles();
        String myVehicleID = connection.getMyVehicle();
        gmh.setVehicles(vehicles);
        Stack<Card> myCards = null; //FIXME
        gmh.setPlayer(new Player(myCards));
        gmh.getPlayer().setVehicle(vehicles.get(vehicles.indexOf(myVehicleID)));
        String mapID = connection.getMapID();
        //TODO: Parse map by ID. OR get map by ID
        Map myMap = null; //FIXME
        gmh.setMap(myMap);
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
