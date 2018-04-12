package com.panic.tdt4240.states;

import com.panic.tdt4240.models.GameInstance;

import java.util.ArrayList;

/**
 * State for running thorught the cards gotten from the server.
 */

public class RunEffectsState extends State {

    private GameInstance gi;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        gi = GameInstance.getInstance();
        runTurnEffects();
    }

    /**
     * Fetch the cards played from the server, and run these.
     */
    void runTurnEffects(){
        ArrayList<String[]> playedCards = new ArrayList<>(); //FIXME Get this from Connection
        gi.playTurn(playedCards);
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
