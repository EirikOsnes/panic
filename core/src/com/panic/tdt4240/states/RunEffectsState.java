package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.view.ViewClasses.RunEffectsView;

import java.util.ArrayList;

/**
 * State for running through the cards gotten from the server.
 */

public class RunEffectsState extends State {

    private GameInstance gi;
    private RunEffectsView runEffectsView;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        gi = GameInstance.getInstance();
        //Connection.getInstance().sendRunEffectsState();
        runEffectsView = new RunEffectsView(this);
    }

    @Override
    public void handleInput(Object o) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        runEffectsView.render();
    }

    @Override
    public void dispose() {
        runEffectsView.dispose();
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new RunEffectsAdapter();
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
