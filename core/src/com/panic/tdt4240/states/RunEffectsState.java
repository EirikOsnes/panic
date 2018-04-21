package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.RunEffectsView;
import com.panic.tdt4240.view.animations.CloudAnimation.AnimationType;
import com.panic.tdt4240.view.animations.Missile.MissileType;

import java.util.ArrayList;

/**
 * State for running through the cards gotten from the server.
 */

public class RunEffectsState extends State implements EventListener {

    private boolean doneParsing = false;
    private boolean serverApproved = false;
    private boolean hasSentEndState = false;
    private boolean defeatedFlag = false;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        view = new RunEffectsView(this);
        EventBus.getInstance().addListener(this);
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

    public void activateSpectate(){
//        System.out.println("ACTIVATING SPECTATE");
        EventBus.getInstance().readyForRemove();
        gsm.set(new PlayCardState(gsm));
    }

    @Override
    public void update(float dt) {
        if(doneParsing && !defeatedFlag){
            if (((RunEffectsView) view).isDoneAnimating()){
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
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
        EventBus.getInstance().removeListener(this);
    }

    @Override
    public AbstractView getView() {
        return view;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new RunEffectsAdapter();
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.MOVE) {
            ((RunEffectsView) view).moveVehicle(e.getInstigatorID(), e.getTargetID());
        }

        else if (e.getT() == Event.Type.DESTROYED) {
            if(GameInstance.getInstance().getPlayer().isAlive()) {
//                System.out.println("Sending destroy from RunEffectsState");
                Connection.getInstance().sendDestroyed(GameInstance.getInstance().getID(), e.getTargetID());
            }
        }

        if (e.getMissileType() != MissileType.NONE) {
            ((RunEffectsView) view).addMissileAnimation(e.getTargetID(), e.getInstigatorID(), e.getMissileType());
        }
        if (e.getCloudType() != AnimationType.NONE) {
            ((RunEffectsView) view).addCloudAnimation(e.getTargetID(), e.getCloudType());

        }
    }

    private class RunEffectsAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            final String[] strings = message.split(":");

            switch (strings[0]){
                case "GET_TURN":
//                    System.out.println("recieved getTurn");
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
                        defeatedFlag = true;
                        GameInstance.getInstance().getPlayer().setAlive(false);
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                ((RunEffectsView) view).defeatMessage();
                                ((RunEffectsView) view).setUpLeaveButton();
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
