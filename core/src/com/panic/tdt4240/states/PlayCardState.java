package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Hand;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;

/**
 * Created by Hermann on 12.03.2018.
 */

public class PlayCardState extends State {

    private PlayCardView playView;
    public Player player;
    private Map map;
    private boolean gameFinished;
    private int playerCount;
    private int playersAlive;
    private ArrayList<Card> cardArrayList;
    private int playedCards;
    private ArrayList<Card> hand;
    private ArrayList<Boolean> selectedCard;

    public PlayCardState(GameStateManager gsm, Player player/*, Map map*/) {
        super(gsm);
        this.player = player;
        //this.map = map;
        cardArrayList = new ArrayList<>();
        playerCount = 2;
        playersAlive = 2;

        hand = player.playCards();
        selectedCard = new ArrayList<>(hand.size());
        for (int i = 0; i < hand.size(); i++) {
            selectedCard.add(i, false);
        }
        playView = new PlayCardView(this);
    }

    @Override
    public void handleInput(Object o) {
        Integer cardIndex = (Integer) o;
        if(playedCards < GlobalConstants.BASE_PLAY_CARDS){
            if(!cardArrayList.contains(hand.get(cardIndex))){
                cardArrayList.add(hand.get(cardIndex));
                selectedCard.set(cardIndex, true);
                playView.cardInfo.setText(hand.get(cardIndex).getTooltip());
                playView.clickedButton(cardIndex, true);
                playedCards++;
            }
            else{
                cardArrayList.remove(hand.get(cardIndex));
                selectedCard.set(cardIndex, false);
                playView.cardInfo.setText("");
                playView.clickedButton(cardIndex, false);
                playedCards--;
            }
        }
        else if(cardArrayList.contains(hand.get(cardIndex))){
            cardArrayList.remove(hand.get(cardIndex));
            selectedCard.set(cardIndex, false);
            playView.cardInfo.setText("");
            playView.clickedButton(cardIndex, false);
            playedCards--;
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
