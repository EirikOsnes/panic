package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.states.State;

/**
 * Created by victor on 05.03.2018.
 *
 *
 *
 *
 *
 * Add generic, useful helper functions here to avoid redundant code.
 *
 */

public abstract class AbstractView {
    protected State state;
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public AbstractView(State state){
        this.state = state;
        //Insert whatever should be used by multiple subclasses
    }

}
