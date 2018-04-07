package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.GameResultsView;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.input;

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
        int x = Gdx.input.getX(); int y = Gdx.input.getY();
        // need some way to find out if the appropriate buttons have been pressed.
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
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
