package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventFactory;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.RunEffectsView;

import java.util.ArrayList;

/**
 * State for running through the cards gotten from the server.
 */

public class RunEffectsState extends State implements EventListener {

    private RunEffectsView runEffectsView;
    private boolean doneParsing = false;
    private boolean serverApproved = false;
    private boolean hasSentEndState = false;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        EventBus.getInstance().addListener(this);
        runEffectsView = new RunEffectsView(this);
        Connection.getInstance().sendRunEffectsState(GameInstance.getInstance().getID());
    }
    public Vehicle getPlayerVehicle(){
        return GameInstance.getInstance().getPlayer().getVehicle();
    }
    public boolean getPlayerAlive(){
        return GameInstance.getInstance().getPlayer().isAlive();
    }

    public void leaveGame(){
        //Connection.getInstance().leaveGame(GameInstance.getInstance().getID());
        gsm.set(new GameResultsState(gsm));
        //gsm.reset();
    }



    @Override
    public void handleInput(Object o) {

    }

    @Override
    public void update(float dt) {
        if(doneParsing){
            if (runEffectsView.isDoneAnimating()){
                if(serverApproved){
                    EventBus.getInstance().readyForRemove();
                    gsm.set(new PlayCardState(gsm));
                }else if(!hasSentEndState){
                    Connection.getInstance().sendEndRunEffectsState(GameInstance.getInstance().getID());
                    hasSentEndState=true;
                }

            }
        }

    }

    @Override
    public void render() {
        runEffectsView.render();
    }

    @Override
    public void dispose() {
        runEffectsView.dispose();
        EventBus.getInstance().removeListener(this);
    }

    @Override
    public AbstractView getView() {
        return runEffectsView;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new RunEffectsAdapter();
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.ATTACK) {
            if (e.getTargetID().matches("A-\\d\\d\\d")) {
                runEffectsView.attackAsteroid(e.getTargetID());
            }
            else if (e.getTargetID().matches("V-\\d\\d\\d")) {
                runEffectsView.attackVehicle(e.getTargetID(),e.getInstigatorID());
            }
        }
        else if (e.getT() == Event.Type.MOVE) {
            runEffectsView.moveVehicle(e.getInstigatorID(), e.getTargetID());
        }
        else if (e.getT() == Event.Type.DESTROYED) {
            Connection.getInstance().sendDestroyed(GameInstance.getInstance().getID(),e.getTargetID());
            runEffectsView.destroyVehicle(e.getTargetID());
        }
    }

    private class RunEffectsAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            final String[] strings = message.split(":");

            switch (strings[0]){
                case "GET_TURN":
                    playTurn(strings);
                    break;
                case "VALID_STATE":
                    serverApproved = true;
                    break;
                case "RESYNC": //strings[1] = REASON
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            gsm.set(new LoadGameState(gsm, GameInstance.getInstance().getID(),true));
                        }
                    });
                    break;
                case "GAME_OVER": //string[1] = VICTORY/DEFEAT/DRAW
                    if (strings[1].equalsIgnoreCase("DEFEAT")){
                        //TODO: Do you wish to spectate? For now, you're sent to GameResultState.
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                leaveGame();
                            }
                        });
                    }else{
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                leaveGame();
                            }
                        });
                    }
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;

            }

        }

        private void playTurn(String[] strings){

            ArrayList<ArrayList<String[]>> turns = GameInstance.getInstance().readTurns(strings[1]);
            if(turns.size()>1){
                Gdx.app.error("MULTIPLETURN_ERROR", "Have received more than one turn in the RunEffectsState...");
            }
            GameInstance.getInstance().playTurn(turns.get(0));
            doneParsing=true;
        }
    }
}
