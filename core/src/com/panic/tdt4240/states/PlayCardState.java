package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

import sun.misc.Queue;

/**
 * Created by Hermann on 12.03.2018.
 */

public class PlayCardState extends State {

    private PlayCardView playView;
    public Player player;
    public Map map;
    private boolean gameFinished;
    private int playerCount;
    private int playersAlive;
    private ArrayList<Card> cardArrayList;

    public PlayCardState(GameStateManager gsm, Player player/*, Map map*/) {
        super(gsm);
        this.player = player;
        //this.map = map;
        cardArrayList = new ArrayList<>();
        playView = new PlayCardView(this);
        playerCount = 2;
        playersAlive = 2;
    }

    @Override
    public void handleInput(Object o) {
        if (o instanceof Card) { // play cards
            if(cardArrayList.contains(o)){
                cardArrayList.remove(o);
            }
            else{
                cardArrayList.add((Card) o);
            }
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
