package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.MapConnections;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

import static com.panic.tdt4240.models.Card.AllowedTarget.ENEMY;
import static com.panic.tdt4240.models.Card.AllowedTarget.PLAYER;
import static com.panic.tdt4240.models.Card.TargetType.ASTEROID;
import static com.panic.tdt4240.models.Card.TargetType.VEHICLE;

/**
 * Created by Hermann on 12.03.2018.
 * State for keeping track of played cards and targets
 */

public class PlayCardState extends State {
    //H: 1794, W 1080
    private PlayCardView playView;
    private Player player;
    private Map map;
    //Order of cards that are played
    private ArrayList<Integer> playedCardsList;
    //Targets for each card
    private ArrayList<String> targets;
    //Number of cards we have played up to this point
    private int numPlayedCards;
    private ArrayList<Card> hand;
    private ArrayList<Boolean> selectedCards;
    //ID of the button we clicked most recently
    private Integer justClicked = -1;
    private boolean isLockedIn = false;

    private MapConnections mapConnections;
    private float timeLeft;
    private boolean enableTimer;
    private boolean vehicleTarget;


    public PlayCardState(GameStateManager gsm) {
        super(gsm);
        enableTimer = false;
        System.out.println("PlayCardState");
        player = GameInstance.getInstance().getPlayer();
        map = GameInstance.getInstance().getMap();

        playedCardsList = new ArrayList<>();
        targets = new ArrayList<>();

        hand = player.playCards();
        selectedCards = new ArrayList<>(hand.size());
        for (int i = 0; i < hand.size(); i++) {
            selectedCards.add(i, false);
        }
        mapConnections = new MapConnections(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        playView = new PlayCardView(this);
        readyForNewTurn();
        if(!GameInstance.getInstance().getPlayer().isAlive()){
            finishRound();
        }
    }

    /**
     * Method to handle input from the PlayCardView
     *
     * @param o ID of the card that has been clicked, if string it is target-ID
     *          if the card already has been selected:
     *          it is deselected and its effect is reverted
     *          else if less than the max number of cards has been selected:
     *          if we should select a target for another card(justClicked != -1), nothing is done
     *          else we set the clicked card to the active card, and wait for its target to be selected
     */
    @Override
    public void handleInput(Object o) {
        System.out.println(o.toString());
        if(!isLockedIn){
            if (o instanceof Integer) {
                Integer handIndex = (Integer) o;

                //Checks if the user wants to deselect an already selected card
                if (selectedCards.get(handIndex)) {
                    //Removes the card and its target from arrays
                    int index = playedCardsList.indexOf(handIndex);
                    playedCardsList.remove(index);
                    //If the card is still selected, i.e target is about to be selected
                    if (justClicked.equals(handIndex)) {
                        playView.setSelectTarget(false);
                        justClicked = -1;
                    }
                    else {
                        targets.remove(index);
                    }
                    selectedCards.set(handIndex, false);
                    playView.resetCurrentButton();
                    numPlayedCards--;
                    playView.clickedButton(handIndex, 0);
                }
                //Checks if the max amount of cards already have been played
                else if (numPlayedCards < GameInstance.getInstance().getPlayer().getAmountPlayedCards()) {
                    justClicked = handIndex;
                    playedCardsList.add(handIndex);
                    selectedCards.set(handIndex, true);
                    numPlayedCards++;

                    if(hand.get(playedCardsList.get(numPlayedCards-1)).getAllowedTarget().equals(PLAYER)){
                        playView.clickedButton(handIndex, -1);
                        playView.setSelectTarget(false);
                        selectTarget(GameInstance.getInstance().getPlayer().getVehicle().getVehicleID());
                    }
                    else{
                        playView.clickedButton(handIndex, 1);
                        playView.setSelectTarget(true);
                    }
                }
            } else if (o instanceof String) {
                vehicleTarget = false;
                selectTarget((String) o);
            }
            System.out.println(playedCardsList);
        }
    }

    /**
     * Saves the target of a card, if a target is waiting to be selected
     *
     * @param s ID of target that has been clicked
     *          if we have selected a card:
     *          add the target as the target of the most recently selected card
     *          reset the justClicked index, allowing us to select a new card and target
     */
    private void selectTarget(String s) {
        String firstTarget;
        String potentialTarget;
        int targetID = s.indexOf("A");
        if (targetID > 0) {
            //Targets vehicle, but can potentially target asteroid instead
            firstTarget = s.substring(0, targetID);
            potentialTarget = s.substring(targetID);
        } else {
            //Targets asteroid
            firstTarget = s;
            potentialTarget = "";
        }
        //If the first target is valid
        if (justClicked > -1 && validTarget(firstTarget)) {
            targets.add(firstTarget);
            playView.clickedButton(justClicked, -1);
            justClicked = -1;
            playView.resetCurrentButton();
            playView.setSelectTarget(false);
        } else {
            //Checks whether we can target the asteroid instead
            if (potentialTarget.length() > 0) {
                selectTarget(potentialTarget);
            } else {
                if (vehicleTarget) {
                    playView.showInvalidTarget("vehicle");
                } else {
                    playView.showInvalidTarget("asteroid");
                }
            }
        }
    }
    public boolean isCardSelected(int index){
        return selectedCards.get(index);
    }

    /**
     * Method for determining validity of target
     *
     * @param targetID asteroid or vehicle id of the target
     * @return whether the target is a valid target
     */
    private boolean validTarget(String targetID) {
        System.out.println(targetID);
        //If the target is an asteroid
        if(targetID.length() > 0){
            return validAsteroidTarget(targetID, numPlayedCards - 1) || validVehicleTarget(targetID, numPlayedCards - 1);
        }
        return false;
    }
    private boolean validAsteroidTarget(String targetID, int index){
        if(targetID.substring(0, 1).equals("A")){
            return hand.get(playedCardsList.get(index)).getTargetType().equals(ASTEROID);
        }
        return false;
    }
    private boolean validVehicleTarget(String targetID, int index){
        if(targetID.substring(0, 1).equals("V")){
            vehicleTarget = true;
            if (hand.get(playedCardsList.get(index)).getTargetType().equals(VEHICLE)) {
                //If the player targets themselves
                if (player.getVehicle().getVehicleID().equals(targetID)) {
                    return !hand.get(playedCardsList.get(index)).getAllowedTarget().equals(ENEMY);
                }
                //The player targets someone else
                return !hand.get(playedCardsList.get(index)).getAllowedTarget().equals(PLAYER);
            }
            return false;
        }
        return false;
    }

    /**
     * Adds connection between two asteroids on the map, if it doesn't already exist
     *
     * @param start          asteroid id
     * @param end            asteroid id
     * @param asteroidWidth  width of sprite, for calculating center
     * @param asteroidHeight height of sprite, for calculating center
     * @param tableHeight    height of table, for calculating buffer height
     */
    public void addConnection(Asteroid start, Asteroid end, float asteroidWidth, float asteroidHeight, float tableHeight) {
        mapConnections.addConnection(start, end, asteroidWidth, asteroidHeight, tableHeight);
    }

    /**
     * @return array of start and endpoints of connecting lines between asteroids
     */
    public ArrayList<Vector2[]> getConnections() {
        return mapConnections.getConnections();
    }

    /**
     * Should be called when the card selection is done
     */
    public void finishRound() {

        isLockedIn = true;
        ArrayList<String[]> result = getCardsAndTargets();
        Connection.getInstance().sendTurn(result,GameInstance.getInstance().getID());
    }

    public boolean isLockedIn() {
        return isLockedIn;
    }

    /**
     * Converts enum cardType to string, ATTACK -> "attack"
     * @param i cardID
     * @return lowercase string of cardType
     */
    public String getCardType(int i){
        return hand.get(i).getCardType().name().toLowerCase();
    }
    public String[] getCardToolTip(int i){
        return hand.get(i).getTooltip().split(" ");
    }
    public int getHandSize(){
        if(!GameInstance.getInstance().getPlayer().isAlive()){
            return 0;
        }
        return hand.size();
    }
    public boolean getPlayerAlive(){
        return GameInstance.getInstance().getPlayer().isAlive();
    }

    public Map getMap(){
        return map;
    }
    public String getColorCar(String id){
        return GameInstance.getInstance().getVehicleById(id).getColorCar();
    }
    public Card getCard(int index){
        return GameInstance.getInstance().getPlayer().getHand().get(index);
    }
    public Vehicle getPlayerVehicle(){
        return player.getVehicle();
    }
    private void setTimeLeft(float timeLeft){
        this.timeLeft = timeLeft;
        enableTimer = true;
        playView.setTimeLeft(timeLeft);
    }
  
    /**
     * Converts the list of cards and targets to a list of actions by the player
     * @return ArrayList with card, target id and id of vehicle that played the card
     */
    private ArrayList<String[]> getCardsAndTargets(){
        ArrayList<String[]> cardsAndTargets = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            String[] playerActions = new String[3];
            playerActions[0] = hand.get(playedCardsList.get(i)).getId();
            playerActions[1] = targets.get(i);
            playerActions[2] = player.getVehicle().getVehicleID();
            cardsAndTargets.add(playerActions);
        }
        return cardsAndTargets;
    }
    public void leaveGame(){
        Connection.getInstance().leaveGame(GameInstance.getInstance().getID());
        //TODO: Send to GameResultState
        gsm.reset();
    }

    @Override
    public void update(float dt) {
        if(enableTimer){
            if(timeLeft > 0){
                timeLeft -= dt;
                playView.update(dt);
            }
        }
    }

    @Override
    public void render() {
        playView.render();
    }

    @Override
    public void dispose() {
        playView.dispose();
    }

    @Override
    public AbstractView getView() {
        return playView;
    }

    /**
     * Tell the server that you are ready to start a new turn
     */
    private void readyForNewTurn(){
        Connection.getInstance().sendPlayCardState(GameInstance.getInstance().getID());
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new PlayCardAdapter();
    }

    private class PlayCardAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "TURN_END":
                    if(!isLockedIn){
                        finishRound();
                    }
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            gsm.set(new RunEffectsState(gsm));
                        }
                    });
                    break;
                case "BEGIN_TURN":
                    setTimeLeft(Float.parseFloat(strings[1]));
                    //EventBus.getInstance().readyForRemove();
                    break;

                case "GAME_OVER": //strings[1] = VICTORY/DEFEAT/DRAW
                    //TODO: Handle this
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;

            }
        }
    }

}
