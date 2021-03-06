package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.XMLParser;
import com.panic.tdt4240.view.ViewClasses.LoadGameView;
import com.panic.tdt4240.view.ViewClasses.AbstractView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * The state for loading up a new game - or reloading a game.
 */

public class LoadGameState extends State implements EventListener {

    private boolean isLoading; //Flag to use for rendering of a loading screen.
    private int lobbyID;
    private boolean resync;

    protected LoadGameState(GameStateManager gsm, int lobbyID, boolean resync) {
        super(gsm);
        this.resync = resync;
        view = new LoadGameView(this);
        this.lobbyID = lobbyID;
        setUpGameInstance();
        EventBus.getInstance().addListener(this);
    }

    /**
     * Resets the GameInstance singleton and sets it up anew with the needed information.
     */
    private void setUpGameInstance(){
        GameInstance.getInstance().reset();
        GameInstance.getInstance().setID(lobbyID);
        isLoading = true;
        Connection.getInstance().getGameInfo(lobbyID);
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
        Connection.getInstance().getLog();

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
        EventBus.getInstance().removeListener(this);
        EventBus.getInstance().readyForRemove();
    }

    @Override
    public AbstractView getView() {
        return view;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new LoadGameAdapter();
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.DESTROYED) {
            if(GameInstance.getInstance().getPlayer().isAlive()) {
                Connection.getInstance().sendDestroyed(GameInstance.getInstance().getID(), e.getTargetID());
                System.out.println("Sending destroy from LoadGameState");
            }
        }
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
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
                case "VALID_STATE":
                    sendToGame();
                    break;
                case "GAME_OVER": //strings[1] = VICTORY/DEFEAT/DRAW
                    if (strings[1].equalsIgnoreCase("DEFEAT")){
                        GameInstance.getInstance().getPlayer().setAlive(false);
                        //TODO: Do you wish to spectate? For now, you're sent to GameResultState.
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                gsm.set(new GameResultsState(gsm));
                            }
                        });
                    }else{
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                gsm.set(new GameResultsState(gsm));
                            }
                        });
                    }
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

            if(strings.length>5 && resync){ //We are resyncing
                GameInstance.getInstance().playTurns(strings[5]);
                Connection.getInstance().sendResyncFinished(lobbyID);
            }else {
                sendToGame();
            }

        }
    }
}
