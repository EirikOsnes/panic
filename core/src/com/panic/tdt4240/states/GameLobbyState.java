package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.view.ViewClasses.GameLobbyView;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyState extends State {

    // fields: player(s), numOfPlayers...
    GameLobbyView view;

    public GameLobbyState(GameStateManager gsm){
        super(gsm);
        view = new GameLobbyView(this);
    }

    public void launchGame(){
        // might want to test this
        gsm.pop(); gsm.pop(); gsm.pop();

        // needs additional parameters?
        //gsm.push(new PlayCardState(gsm));
    }

    @Override
    public void handleInput(Object o) {
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
