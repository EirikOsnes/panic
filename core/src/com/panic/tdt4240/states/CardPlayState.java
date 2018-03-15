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

    public CardPlayState(GameStateManager gsm/*, Player player, Map map*/) {
        super(gsm);
        //this.player = player;
        player.playCards();
        //this.map = map;
        playView = new PlayCardView(this);
    }

    @Override
    public void handleInput(Object o) {
        if (o.equals(0)) { // play cards

        }
        if (o.equals(1)){ // animation

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
        playView.render(sb);

    }

    @Override
    public void dispose() {
        playView.dispose();
    }
}
