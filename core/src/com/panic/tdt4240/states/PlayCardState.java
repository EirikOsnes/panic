package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

import java.util.ArrayList;

import static com.panic.tdt4240.models.Card.AllowedTarget.ENEMY;
import static com.panic.tdt4240.models.Card.AllowedTarget.PLAYER;
import static com.panic.tdt4240.models.Card.TargetType.ASTEROID;
import static com.panic.tdt4240.models.Card.TargetType.VEHICLE;

/**
 * Created by Hermann on 12.03.2018.
 */

public class PlayCardState extends State {
    //H: 1794, W 1080
    private PlayCardView playView;
    private Player player;
    private Map map;
    //Order of cards that are played
    private ArrayList<Card> playedCardsList;
    //Targets for each card
    private ArrayList<String> targets;
    //Number of cards we have played up to this point
    private int numPlayedCards;
    private ArrayList<Card> hand;
    private ArrayList<Boolean> selectedCard;
    private ArrayList<AsteroidConnection> connections;
    private ArrayList<Vehicle> vehicles;
    //ID of the button we clicked most recently
    private Integer justClicked = -1;
    private GameInstance gameInstance;

    public PlayCardState(GameStateManager gsm) {
        super(gsm);
        gameInstance = GameInstance.getInstance();

        player = gameInstance.getPlayer();
        map = gameInstance.getMap();
        vehicles = gameInstance.getVehicles();

        playedCardsList = new ArrayList<>();
        targets = new ArrayList<>();
        connections = new ArrayList<>();

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
        System.out.println(o.toString());
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
                    playView.setCardInfoText(playedCardsList.get(playedCardsList.size() - 1).getTooltip());
                } else {
                    playView.setCardInfoText("");
                }
                playView.clickedButton(cardIndex, 0);
                numPlayedCards--;
            }
            //Checks if the max amount of cards already have been played
            else if (numPlayedCards < player.getAmountPlayedCards()) {
                if (justClicked == -1) {
                    justClicked = cardIndex;
                    playedCardsList.add(hand.get(cardIndex));
                    selectedCard.set(cardIndex, true);
                    playView.setCardInfoText(hand.get(cardIndex).getTooltip());
                    playView.clickedButton(cardIndex, 1);
                    numPlayedCards++;
                    playView.setSelectTarget(true);
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
    //FIXME Click on valid target asteroid should end select target phase
    private void selectTarget(String s){
        s = s.toLowerCase();
        String firstTarget;
        String potentialTarget;
        int targetID = s.indexOf("a");
        if(targetID > 0){
            //Targets vehicle, but can potentially target asteroid instead
            firstTarget = s.substring(0, targetID);
            potentialTarget = s.substring(targetID);
        }
        else{
            //Targets asteroid
            firstTarget = s;
            potentialTarget = "";
        }
        //If the first target is valid
        if(justClicked > -1 && validTarget(firstTarget)){
            targets.add(firstTarget);
            playView.clickedButton(justClicked, -1);
            justClicked = -1;
            playView.setSelectTarget(false);
        }
        else{
            //Checks whether we can target the asteroid instead
            if(potentialTarget.length() > 0){
                selectTarget(potentialTarget);
            }
            else{
                //TODO Should show 'not valid targed' in PlayCardView
            }
        }
    }
    /**
     * Method for determining validity of target
     * @param targetID asteroid or vehicle id of the target
     * @return whether the target is a valid target
     */
    private boolean validTarget(String targetID){
        System.out.println(targetID);
        //If the target is an asteroid
        if(targetID.substring(0, 1).equals("a")){
            return playedCardsList.get(numPlayedCards-1).getTargetType().equals(ASTEROID);
        }
        //If the target is a vehicle
        else if(targetID.substring(0, 1).equals("v")){
            //If the player can target a vehicle
            if(playedCardsList.get(numPlayedCards-1).getTargetType().equals(VEHICLE)){
                //If the player targets themselves
                if(player.getVehicle().getVehicleID().toLowerCase().equals(targetID)){
                    return !playedCardsList.get(numPlayedCards-1).getAllowedTarget().equals(ENEMY);
                }
                //The player targets someone else
                return !playedCardsList.get(numPlayedCards-1).getAllowedTarget().equals(PLAYER);
            }
            return false;
        }
        return false;
    }

    /**
     * Adds connection between two asteroids on the map, if it doesn't already exist
     * @param start asteroid id
     * @param end asteroid id
     * @param asteroidWidth width of sprite, for calculating center
     * @param asteroidHeight height of sprite, for calculating center
     * @param tableHeight height of table, for calculating buffer height
     */
    public void addConnection(Asteroid start, Asteroid end, float asteroidWidth, float asteroidHeight, float tableHeight){
        if(notConnected(start.getId(), end.getId())){
            AsteroidConnection connection = new AsteroidConnection(
                    //Calculation of center point of the asteroids, see setUpMap() in PlayCardView
                    new Vector2(start.getPosition().x *(Gdx.graphics.getWidth() - asteroidWidth) + asteroidWidth/2,
                            start.getPosition().y *(Gdx.graphics.getHeight() - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    new Vector2(end.getPosition().x *(Gdx.graphics.getWidth() - asteroidWidth) + asteroidWidth/2,
                            end.getPosition().y *(Gdx.graphics.getHeight() - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    start.getId(), end.getId());
            connections.add(connection);
        }
    }

    /**
     * Checks if an equivalent connection has already been added
     * @param startID start asteroid
     * @param endID end asteroid
     * @return whether this connection already exists
     */
    private boolean notConnected(String startID, String endID){
        for(AsteroidConnection connection: connections){
            if(connection.startID.equals(endID) && connection.endID.equals(startID)){
                return false;
            }
            else if(connection.startID.equals(startID) && connection.endID.equals(endID)){
                return false;
            }
        }
        return true;
    }

    /**
     * @return array of start and endpoints of connecting lines between asteroids
     */
    public ArrayList<Vector2[]> getConnections(){
        ArrayList<Vector2[]> lines = new ArrayList<>();
        for(AsteroidConnection connection : connections){
            Vector2[] line = new Vector2[2];
            line[0] = connection.start;
            line[1] = connection.end;
            lines.add(line);
        }
        return lines;
    }

    /**
     * Should be called when the card selection is done
     */
    public ArrayList<String[]> finishRound(){
        ArrayList<String[]> result = getCardsAndTargets();
        //TODO Avslutt viewet m.m
        return result;
    }

    /**
     * Converts enum cardtype to string, ATTACK -> "attack"
     * @param i cardID
     * @return lowercase string of cardtype
     */
    public String getCardType(int i){
        return hand.get(i).getCardType().name().toLowerCase();
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

    }

    @Override
    public void render() {
        playView.render();
    }

    @Override
    public void dispose() {
        playView.dispose();
    }

    /**
     * For an asteroid, gives coordinates within asteroid for each car type
     * Positions are clockwise from lower left section
     * Position of the lower left corner of each section
     */
    public Vector2 AsteroidPositions(float posX, float posY, float width, float height, String colorCar){
        Vector2 position = new Vector2(posX + width/9, posY);
        switch (colorCar){
            case "red_car":
                position.add(0,0);
                break;
            case "green_car":
                position.add(0, height/2);
                break;
            case "yellow_car":
                position.add(width/2, height/2);
                break;
            case "blue_car":
                position.add(width/2, 0);
                break;
        }
        return position;
    }

    /**
     * Class for keeping track of connections between asteroids, for rendering in PlayCardView
     * Has id for start and end asteroid, and their coordinates
     */
    private class AsteroidConnection {
        private Vector2 start;
        private Vector2 end;
        private String startID;
        private String endID;
        private AsteroidConnection(Vector2 start, Vector2 end, String startID, String endID){
            this.start = start;
            this.startID = startID;
            this.end = end;
            this.endID = endID;
        }
    }

}
