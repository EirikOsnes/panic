package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventFactory;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.view.ViewClasses.RunEffectsView;

import java.util.ArrayList;

/**
 * State for running through the cards gotten from the server.
 */

public class RunEffectsState extends State implements EventListener {

    private GameInstance gi;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        gi = GameInstance.getInstance();
        EventBus.getInstance().addListener(this);
        //Connection.getInstance().sendRunEffectsState();
        view = new RunEffectsView(this);
        Card c = ModelHolder.getInstance().getCardById("MOVE");
        EventFactory.postEventsFromCard(c, "A-003", "V-001");
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
        ((RunEffectsView) view).dispose();
        EventBus.getInstance().removeListener(this);
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new RunEffectsAdapter();
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.ATTACK) {
            if (e.getTargetID().matches("A-\\d\\d\\d")) {
                ((RunEffectsView) view).attackAsteroid(e.getTargetID());
            }
            else if (e.getTargetID().matches("V-\\d\\d\\d")) {
                ((RunEffectsView) view).attackVehicle(e.getTargetID());
            }
        }
        else if (e.getT() == Event.Type.MOVE) {
            ((RunEffectsView) view).moveVehicle(e.getInstigatorID(), e.getTargetID());
        }

        else if (e.getT() == Event.Type.DESTROYED) {
            ((RunEffectsView) view).destroyVehicle(e.getTargetID());
        }
    }

    private class RunEffectsAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "GET_TURN":
                    ArrayList<ArrayList<String[]>> turns = GameInstance.getInstance().readTurns(strings[1]);
                    if(turns.size()>1){
                        Gdx.app.error("MULTIPLETURN_ERROR", "Have received more than one turn in the RunEffectsState...");
                    }
                    GameInstance.getInstance().playTurn(turns.get(0));
                    break;

            }
        }
    }
}
