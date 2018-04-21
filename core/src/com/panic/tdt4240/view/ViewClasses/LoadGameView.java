package com.panic.tdt4240.view.ViewClasses;

import com.panic.tdt4240.states.State;
import com.panic.tdt4240.view.ViewClasses.AbstractView;

/**
 * Created by Hermann on 17.04.2018.
 * 
 * This view was supposed to show a loading indicator, e.g. a rotating
 * refresh symbol similar to server browsers' refresh button.
 *
 * As it is not important for the application, it was mostly ignored
 * and left as is, also due to time constraints.
 *
 */

public class LoadGameView extends AbstractView {


    public LoadGameView(State state) {
        super(state);

    }

    @Override
    public void render() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
