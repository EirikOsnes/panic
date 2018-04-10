package com.panic.tdt4240.states;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

import static com.panic.tdt4240.models.Card.AllowedTarget.ENEMY;
import static com.panic.tdt4240.models.Card.TargetType.ASTEROID;
import static com.panic.tdt4240.models.Card.TargetType.VEHICLE;

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
    //Order of cards that are played
    private ArrayList<Card> playedCardsList;
    //Targets for each card
    private ArrayList<String> targets;
    //Number of cards we have played up to this point
    private int numPlayedCards;
    private ArrayList<Card> hand;
    private ArrayList<Boolean> selectedCard;
    //ID of the button we clicked most recently
    private Integer justClicked = -1;

    public PlayCardState(GameStateManager gsm, Player player, Map map) {
        super(gsm);
        this.player = player;
        this.map = map;
        playedCardsList = new ArrayList<>();
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
     * @param o ID of the card that has been clicked, if string it is target-ID
     *      if the card already has been selected:
     *          it is deselected and its effect is reverted
     *      else if less than the max number of cards has been selected:
     *          if we should select a target for another card(justClicked != -1), nothing is done
     *          else we set the clicked card to the active card, and wait for its target to be selected
     */
    @Override
    public void handleInput(Object o) {
        if (o instanceof Integer) {
            Integer cardIndex = (Integer) o;

            //Checks if the user wants to deselect an already selected card
            if (selectedCard.get(cardIndex)) {
                //Removes the card and its target from arrays
                int index = playedCardsList.indexOf(hand.get(cardIndex));
                playedCardsList.remove(index);
                //If the card is still selected, ie target is about to be selected
                if (justClicked.equals(cardIndex)) {
                    justClicked = -1;
                } else {
                    targets.remove(index);
                }
                selectedCard.set(cardIndex, false);

                //Sets the tooltip text to the most recently pressed card, or to an empty string
                if (playedCardsList.size() > 0) {
                    playView.cardInfo.setText(playedCardsList.get(playedCardsList.size() - 1).getTooltip());
                } else {
                    playView.cardInfo.setText("");
                }
                playView.clickedButton(cardIndex, false);
                numPlayedCards--;
            }
            //Checks if the max amount of cards already have been played
            else if (numPlayedCards < player.getAmountPlayedCards()) {
                if (justClicked == -1) {
                    justClicked = cardIndex;
                    playedCardsList.add(hand.get(cardIndex));
                    selectedCard.set(cardIndex, true);
                    playView.cardInfo.setText(hand.get(cardIndex).getTooltip());
                    playView.clickedButton(cardIndex, true);
                    numPlayedCards++;
                    playView.selectTarget = true;
                }
            }
        }
        else if(o instanceof String){
            selectTarget((String) o);
        }
    }

    /**
     * Saves the target of a card, if a target is waiting to be selected
     * @param s ID of target that has been clicked
     *      if we have selected a card:
     *          add the target as the target of the most recently selected card
     *          reset the justClicked index, allowing us to select a new card and target
     */
    private void selectTarget(String s){
        if(justClicked > -1 && validTarget(s)){
            targets.add(s);
            playView.clickedButton(justClicked, true);
            justClicked = -1;
            playView.selectTarget = false;
        }
        else{
            //TODO Should show 'not valid targed' in PlayCardView
        }
    }

    /**
     * Method for determining validity of target
     * @param targetID asteroid or vehicle id of the target
     * @return whether the target is a valid targer
     */
    private boolean validTarget(String targetID){
        String attackType = targetID.substring(0,1).toLowerCase();
        //If the target is an asteroid
        if(attackType.equals("a")){
            return playedCardsList.get(numPlayedCards-1).getTargetType().equals(ASTEROID);
        }
        //If the target is a vehicle
        else if(attackType.equals("v")){
            //If the player targets themselves
            if(player.getVehicle().getVehicleID().equals(targetID)){
                return !playedCardsList.get(numPlayedCards-1).getAllowedTarget().equals(ENEMY);
            }
            return playedCardsList.get(numPlayedCards-1).getTargetType().equals(VEHICLE);
        }
        return false;
    }
    /**
     * Converts the list of cards and targets to a list of actions by the player
     * @return ArrayList with card, target id and id of vehicle that played the card
     */
    public ArrayList<String[]> getCardsAndTargets(){
        ArrayList<String[]> cardsAndTargets = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            String[] playerActions = new String[3];
            playerActions[0] = playedCardsList.get(i).getId();
            playerActions[1] = targets.get(i);
            playerActions[2] = player.getVehicle().getVehicleID();
            cardsAndTargets.add(playerActions);
        }
        return cardsAndTargets;
    }

    @Override
    public void update(float dt) {
        // e.g. winner=null, if all players left the lobby.
        //if (playerCount < 2) gsm.push(new GameResultsState(gsm));
        //if (playersAlive==1) gsm.push(new GameResultsState(gsm));
    }

    @Override
    public void render() {
        playView.render();
    }

    @Override
    public void dispose() {
        playView.dispose();
    }

}
