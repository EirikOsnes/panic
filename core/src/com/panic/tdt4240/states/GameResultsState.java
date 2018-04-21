package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.GameResultsView;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 */

public class GameResultsState extends State {

    public GameResultsState(GameStateManager gsm){ // winner = last index
        super(gsm);
        view = new GameResultsView(this);
        Connection.getInstance().gameOverInfo(GameInstance.getInstance().getID());
    }

    public void updateInfo(String[] strings){
        ((GameResultsView) view).setLabelText(strings[1]);
    }

    @Override
    public void handleInput(Object o) {
    if ((Integer) o == -1){ // back to main menu. Reset entirely.
            Connection.getInstance().leaveGame(GameInstance.getInstance().getID());
            gsm.reset();
        }
    }

    @Override
    public void update(float dt) {
        // nothing to do here, really.
    }

    @Override
    public void render() {
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    @Override
    public AbstractView getView() {
        return view;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new GameResultsAdapter();
    }

    private class GameResultsAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            final String[] strings = message.split(":");

            switch (strings[0]){
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
                case "GAME_OVER_INFO":
                    updateInfo(strings);
                    break;

            }
        }
    }
}
