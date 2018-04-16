package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;
import com.panic.tdt4240.view.ViewClasses.SettingsView;

/**
 * Created by magnus on 12.03.2018.
 */

public class SettingsState extends State {

    private SettingsView view;

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

            /* e.printStackTrace(); /**/
            return false;
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        view.render();
    }

    @Override
    public void dispose() {

    }

    @Override
    public PlayCardView getView() {
        return view;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new SettingsAdapter();
    }

    private class SettingsAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {

        }
    }
}
