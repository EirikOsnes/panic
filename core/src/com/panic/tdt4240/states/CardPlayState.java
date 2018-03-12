package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Hermann on 12.03.2018.
 */

public class CardPlayState extends State {

    protected CardPlayState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

        //Send sb and texture, positions... to correct view

    }

    @Override
    public void dispose() {

    }
}
