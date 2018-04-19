package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.XMLParser;
import com.panic.tdt4240.view.LoadGameView;
import com.panic.tdt4240.view.ViewClasses.AbstractView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * The state for loading up a new game - or reloading a game.
 */

public class LoadGameState extends State {

    private Connection connection;
    private boolean isLoading; //Flag to use for rendering of a loading screen.
    private LoadGameView view;
    private int lobbyID;

    protected LoadGameState(GameStateManager gsm, int lobbyID) {
        super(gsm);
        view = new LoadGameView(this);
        connection = Connection.getInstance();
        this.lobbyID = lobbyID;
        setUpGameInstance();
        view = new LoadGameView(this);
    }

    /**
     * Resets the GameInstance singleton and sets it up anew with the needed information.
     */
    private void setUpGameInstance(){
        GameInstance.getInstance().reset();
        GameInstance.getInstance().setID(lobbyID);
        isLoading = true;
        connection.getGameInfo(lobbyID);
    }

    private void setGIValues(ArrayList<Vehicle> vehicles, String mapID, String myVehicleID, String seedString){
        Vehicle myVehicle = null;
        for (Vehicle v : vehicles) {
            if (v.getVehicleID().equals(myVehicleID))
                myVehicle = v;
        }
        GameInstance.getInstance().setVehicles(vehicles);
        XMLParser parser = new XMLParser();
        Stack<Card> myCards = parser.parseCardStack(myVehicle.getVehicleType());
        GameInstance.getInstance().setPlayer(new Player(myCards));
        GameInstance.getInstance().getPlayer().setVehicle(myVehicle);
        Map myMap = ModelHolder.getInstance().getMapById(mapID);
        GameInstance.getInstance().setMap(myMap);
        GameInstance.getInstance().setSeed(Long.parseLong(seedString));
        isLoading = false;
    }

    private void sendToGame(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                gsm.pop();
                gsm.set(new PlayCardState(gsm));
            }
        });
    }

    private void setUpVehiclePositions(ArrayList<Vehicle> vehicles, long seed){
        Random random = new Random(seed);
        ArrayList<Asteroid> asteroids = GameInstance.getInstance().getAsteroids();

        for (Vehicle v : vehicles) {
            int pos = random.nextInt(asteroids.size());
            asteroids.get(pos).addVehicle(v.getVehicleID());
        }

    }


    /**
     * Checks to see if the client is reconnecting - i.e. there is a history of cards played, and plays these if that is the case.
     */
    private void checkForHistory() {
        connection.getLog();

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
                case "GET_LOG":
                    GameInstance.getInstance().playTurns(strings[1]);
                    break;

            }

        }

        private void parseGameInfo(String[] strings){
            String[] vehicleStrings = strings[1].split("&");
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            for (String vehicleString : vehicleStrings) {
                String[] vehicleInfo = vehicleString.split(",");
                Vehicle myVehicle = ModelHolder.getInstance().getVehicleByName(vehicleInfo[0]).cloneVehicleWithId(vehicleInfo[1]);
                myVehicle.setColorCar(vehicleInfo[2].toLowerCase()+"_car");
                vehicles.add(myVehicle);
            }

            setGIValues(vehicles, strings[2], strings[3],strings[4]);

            setUpVehiclePositions(vehicles,Long.parseLong(strings[4]));

            if(strings.length>5){
                GameInstance.getInstance().playTurns(strings[5]);
            }

            sendToGame();

        }
    }
}
