package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.panic.tdt4240.states.State;

/**
 * Created by victor on 12.03.2018.
 *
 * This would be among the last features to implement. Due to time constraints,
 * this is unfinished and therefore hidden from users.
 */

public class SettingsView extends AbstractView {

    Slider musicVolume;
    Slider sfxVolume;

    public SettingsView(State state) {
        super(state);
    }

    public void render(){
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }

}
