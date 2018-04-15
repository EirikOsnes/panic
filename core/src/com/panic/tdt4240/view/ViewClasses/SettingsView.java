package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.panic.tdt4240.states.State;

/**
 * Created by victor on 12.03.2018.
 *
 */


public class SettingsView extends AbstractView {

    Slider musicVolume;
    Slider sfxVolume;

    public SettingsView(State state) {
        super(state);
    }

    public void render(){
    }

    public void dispose(){

    }

}
