package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.connection.ICallbackAdapter;

/**
 * Created by magnus on 12.03.2018.
 */

public abstract class State {
    protected Vector2 mouse;
    protected GameStateManager gsm;
    protected ICallbackAdapter callbackAdapter;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        mouse = new Vector2();
        setUpAdapter();
    }

    protected abstract void setUpAdapter();
    public abstract void handleInput(Object o);
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
