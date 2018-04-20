package com.panic.tdt4240.view.ViewClasses;

import com.panic.tdt4240.states.State;
import com.panic.tdt4240.view.ViewClasses.AbstractView;

/**
 * Created by Hermann on 17.04.2018.
 */

public class LoadGameView extends AbstractView {


    public LoadGameView(State state) {
        super(state);

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
