package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

/**
 * Created by Hermann on 12.03.2018.
 */

public class CardPlayState extends State {

    private PlayCardView playView;
    public Player player;
    public Map map;
    private boolean gameFinished;
    private int playerCount;
    private int playersAlive;

    protected CardPlayState(GameStateManager gsm) {
        super(gsm);
        playView = new PlayCardView(this);
        //TODO Add player instance, + map
    }

    @Override
    public void handleInput(Object o) {
        if (o == 0) { // play cards

        }
        if (o == 1){ // animation

        }
    }

    @Override
    public void update(float dt) {
        // TODO: pass appropriate data to gameresults (which then passes to view)
        // e.g. winner=null, if all players left the lobby.
        if (playerCount < 2) gsm.push(new GameResultsState(gsm));
        if (playersAlive==1) gsm.push(new GameResultsState(gsm));
    }

    @Override
    public void render(SpriteBatch sb) {


    }

    @Override
    public void dispose() {
        playView.dispose();
    }
}
