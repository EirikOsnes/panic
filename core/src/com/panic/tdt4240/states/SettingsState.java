package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.view.ViewClasses.SettingsView;

/**
 * Created by magnus on 12.03.2018.
 */

public class SettingsState extends State {

    SettingsView view;

    public SettingsState(GameStateManager gsm){
        super(gsm);
        view = new SettingsView(this);
    }

    @Override
    public void handleInput(Object o) {
        if (o== (Integer) 1) exit();
    }

    public void exit(){
        gsm.pop();
    }

    public boolean saveSettings(){
        try {
            return true;
        }
        catch (Exception e){
            // popup with error message?

            return false;
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
