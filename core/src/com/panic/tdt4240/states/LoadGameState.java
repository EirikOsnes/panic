package com.panic.tdt4240.states;

import com.panic.tdt4240.models.GameModelHolder;

/**
 * Created by Eirik on 07-Apr-18.
 */

public class LoadGameState extends State {

    GameModelHolder gmh;

    protected LoadGameState(GameStateManager gsm) {
        super(gsm);
        gmh = GameModelHolder.getInstance();

    }

    private void setUpGameModelHolder(){
        gmh.reset();
        //TODO: Get the vehicle types of all players, and what Vehicle is to be assigned to the current player, so these can be added to gmh.
        //TODO: Get the deck for the current player, so the Player can be added to the gmh.
        //TODO: Set up the Map, and add the asteroids
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
