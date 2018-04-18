package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.GameResultsView;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 */

public class GameResultsState extends State {

    GameResultsView view;
    int originalNumOfPlayers;
    int numOfPlayers;

    public GameResultsState(GameStateManager gsm, ArrayList<Player> deadPlayers){ // winner = last index
        super(gsm);
        view = new GameResultsView(this, deadPlayers);
    }

    @Override
    public void handleInput(Object o) {
        String s = (String) o;
        if (o == (Integer) 0) { // go back to lobby
            gsm.pop(); // to CardPlayState
            gsm.pop(); // to GameLobbyState
            if (originalNumOfPlayers != numOfPlayers) {
                // TODO : some players left the lobby. Update/replace lobby?
            }
            else {
                gsm.pop(); // to GameLobbyState
            }
        }
    else if ((int) o== -1){ // back to main menu. Reset entirely.
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

        }
    }
}
