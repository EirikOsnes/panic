package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.view.ViewClasses.GameResultsView;

/**
 * Created by victor on 12.03.2018.
 */

public class GameResultsState extends State {

    GameResultsView view;
    int originalNumOfPlayers;
    int numOfPlayers;

    public GameResultsState(GameStateManager gsm){
        super(gsm);
        view = new GameResultsView(this);
    }

    @Override
    public void handleInput(Object o) {
        if (o== (Integer) 0) { // back to lobby
            gsm.pop(); // to CardPlayState
            gsm.pop(); // to GameLobbyState
            if (originalNumOfPlayers != numOfPlayers) {
            // TODO: some players left the lobby... should the lobby simply be updated, or replaced?
            }
        }
    else if (o== (Integer) 1){ // back to main menu. Pop some states, set new meu state.
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
