package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

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
    //Order of cards that are played
    private ArrayList<Card> cardArrayList;
    //Targets for each card
    private ArrayList<Integer> targets;
    //Number of cards we have played up to this point
    private int playedCards;
    private ArrayList<Card> hand;
    private ArrayList<Boolean> selectedCard;
    //ID of the button we clicked most recently
    private Integer justClicked = -1;

    public PlayCardState(GameStateManager gsm, Player player/*, Map map*/) {
        super(gsm);
        this.player = player;
        //this.map = map;
        cardArrayList = new ArrayList<>();
        targets = new ArrayList<>();

        playerCount = 2;
        playersAlive = 2;

        hand = player.playCards();
        selectedCard = new ArrayList<>(hand.size());
        for (int i = 0; i < hand.size(); i++) {
            selectedCard.add(i, false);
        }
        playView = new PlayCardView(this);
    }

    /**
     * Method to handle input from the PlayCardView
     * @param o Integer id of the card that has been clicked
     *      if the card already has been selected:
     *          it is deselected and its effect is reverted
     *      else if less than the max number of cards has been selected:
     *          if we should select a target for another card(justClicked != -1), nothing is done
     *          else we set the clicked card to the active card, and wait for its target to be selected
     */
    @Override
    public void handleInput(Object o) {
        Integer cardIndex = (Integer) o;
        //Checks if the user wants to deselect an already selected card
        if(selectedCard.get(cardIndex)) {
            //Removes the card and its target from arrays
            int index = cardArrayList.indexOf(hand.get(cardIndex));
            cardArrayList.remove(index);
            //If the card is still selected, ie target is about to be selected
            if(justClicked.equals(cardIndex)){
                justClicked = -1;
            }
            else{
                targets.remove(index);
            }
            selectedCard.set(cardIndex, false);

            //Sets the tooltip text to the most recently pressed card, or to an empty string
            if(cardArrayList.size() > 0) {
                playView.cardInfo.setText(cardArrayList.get(cardArrayList.size()-1).getTooltip());
            }
            else{
                playView.cardInfo.setText("");
            }
            playView.clickedButton(cardIndex, false);
            playedCards--;
        }
        //Checks if the max amount of cards already have been played
        else if(playedCards < player.getAmountPlayedCards()) {
            if(justClicked == -1){
                justClicked = cardIndex;
                cardArrayList.add(hand.get(cardIndex));
                selectedCard.set(cardIndex, true);
                playView.cardInfo.setText(hand.get(cardIndex).getTooltip());
                playView.clickedButton(cardIndex, true);
                playedCards++;
                playView.selectTarget = true;
            }
        }
    }

    /**
     * Saves the target of a card, if a target is waiting to be selected
     * @param i ID of target that has been clicked
     *      if we have selected a card:
     *          add the target as the target of the most recently selected card
     *          reset the justClicked index, allowing us to select a new card and target
     */
    public void selectTarget(Integer i){
        if(justClicked > -1){
            targets.add(i);
            justClicked = -1;
            playView.selectTarget = false;
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
