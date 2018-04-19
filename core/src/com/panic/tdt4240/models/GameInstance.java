package com.panic.tdt4240.models;

import com.panic.tdt4240.events.EventFactory;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class to hold all Game-specific information such as the Map, the vehicles and the player, as well as some general methods.
 */

public class GameInstance {

    private static GameInstance gi;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Asteroid> asteroids;
    private Map map;
    private Player player;
    private int ID;
    private long seed;

    private GameInstance(){
        this.vehicles = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.map = null;
    }

    public static GameInstance getInstance(){
        if(gi == null){
            gi = new GameInstance();
        }
        return gi;
    }

    public void setMap(Map map) {
        this.map = map;

        setAsteroids(map.getAsteroids());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSeed(long seed){
        this.seed = seed;
    }

    public void addVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }

    private void setAsteroids(ArrayList<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public Map getMap() {
        return map;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String locateVehicle(String vehicleID){
        for (Asteroid asteroid : asteroids) {
            if(asteroid.getVehicles().contains(vehicleID)){
                return asteroid.getId();
            }
        }
        return null;
    }

    public Vehicle getVehicleById(String ID){
        for (Vehicle v : vehicles) {
            if(v.getVehicleID().equalsIgnoreCase(ID))
                return v;
        }

        return null;
    }

    public Asteroid getAsteroidById(String ID){
        for (Asteroid a : asteroids) {
            if(a.getId().equalsIgnoreCase(ID))
                return a;
        }

        return null;
    }


    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void reset(){
        gi = new GameInstance();
    }


    /**
     * reads the history of the game. If the game has no history, the method returns null. The history string needs to be formatted as "CARDID&SENDERID&TARGETID&SEED//" where turns get separated
     * with "ENDTURN//".
     *
     * @param turns The turns String
     * @return An arrayList containing ArrayLists of CardIDs, SenderIDs, TargetIDs and Seeds
     */
    public ArrayList<ArrayList<String[]>> readTurns(String turns){
        if (turns.equals("")) {
            return null;
        }

        String[] data = turns.split("//");
        ArrayList<ArrayList<String[]>> result = new ArrayList<>();
        ArrayList<String[]> currentTurn = new ArrayList<>();
        for (String string : data){
            if(string.equals("TURNEND")){
                result.add(currentTurn);
                currentTurn = new ArrayList<>();
            } else {
                String[] elements = string.split("&");
                if (elements.length != 4) {
                    throw new IllegalArgumentException("String is not formatted correctly");
                }
                currentTurn.add(new String[]{elements[0],elements[2],elements[1],elements[3]});
            }
        }

        return result;
    }

    /**
     * Play out a turn. This method will create all events and check targeting.
     * @param playedCards An ArrayList of String[]. Each String[] represents 1 card, on the form {CardID, targetID, instigatorID, seed}.
     */
    public void playTurn(ArrayList<String[]> playedCards){

        EventFactory.postNewTurnEvent();

        Random random = new Random(seed);
        for (String[] s : playedCards) {
            s[1] = s[1].toUpperCase();
            Card card = ModelHolder.getInstance().getCardById(s[0]);
            float multiplier = 1;
            if (card.getCardType() == Card.CardType.ATTACK){
                if (s[2].charAt(0)=='A'){
                    multiplier = getAsteroidById(s[2]).getStatusHandler().getDamageModifier();
                }else if(s[2].charAt(0)=='V'){
                    multiplier = getVehicleById(s[2]).getStatusHandler().getDamageModifier();
                }
            }
            ArrayList<String> validTargets = getAllValidTargets(card, s[2]);
            if(validTargets.contains(s[1])){
                //EventFactory.postEventsFromCard(card,s[1],s[2],multiplier);
                card.playCard(s[1],s[2]);
            }
            else {
                if(validTargets.size()>0) {
                    int index = random.nextInt(validTargets.size());
                    card.playCard(validTargets.get(index),s[2]);
                    //EventFactory.postEventsFromCard(card,validTargets.get(index),s[2],multiplier);
                }

                else{
                    //TODO: No valid targets - how to make the card go away? For visuals.
                }

            }
        }

        EventFactory.postEndTurnEvent();
    }

    /**
     * Parse and play multiple turns from a log
     * @param strings The log string
     */
    public void playTurns(String strings){
        for (ArrayList<String[]> s : readTurns(strings)){
            playTurn(s);
        }
    }

    private ArrayList<String> getAllValidTargets(Card card, String instigatorID){
        ArrayList<String> result = new ArrayList<>();

        //Find the asteroid the instigator is located at - for range checks
        String startPosition = instigatorID;
        if(instigatorID.charAt(0) == 'A'){
            startPosition = instigatorID;
        }else if(instigatorID.charAt(0)=='V'){
            startPosition = gi.locateVehicle(instigatorID);

            if(startPosition == null){
                return result;
            }
        }

        //Find the applicable adjacency row.
        int[][] adjacency = gi.getMap().getAdjacency();
        int index = Integer.parseInt(startPosition.substring(2))-1;
        int[] neighbours = adjacency[index];

        //Get min and max range
        float minRange = card.getMinRange();
        float maxRange = card.getMaxRange();

        //If the card is a movement card, check for the vehicles movement modifier.
        if (card.getCardType() == Card.CardType.MOVEMENT && instigatorID.charAt(0)=='V'){
            minRange = Math.min(minRange,minRange*getVehicleById(instigatorID).getStatusHandler().getMovementModifier());
            maxRange = maxRange*getVehicleById(instigatorID).getStatusHandler().getMovementModifier();
        }

        //Create list of all asteroids within range
        ArrayList<Asteroid> validAsteroids = new ArrayList<>();
        for (int i = 0; i < neighbours.length; i++) {
            if(neighbours[i]>=minRange && neighbours[i]<=maxRange){
                validAsteroids.add(gi.getMap().getAsteroids().get(i));
            }
        }

        //Remove any asteroids that have been destroyed from the list. Will probably never be a case.
        for (int i = validAsteroids.size()-1; i >=0 ; i--) {
            if(validAsteroids.get(i).isDestroyed()){
                validAsteroids.remove(i);
            }
        }

        //If target is an asteroid, all asteroids within range will be valid targets
        if(card.getTargetType() == Card.TargetType.ASTEROID){
            for (Asteroid asteroid : validAsteroids) {
                result.add(asteroid.getId());
            }
            return result;
        }

        //If target is a vehicle, we have to add all vehicles on them to the valid targets list
        if(card.getTargetType() == Card.TargetType.VEHICLE){
            for (Asteroid asteroid : validAsteroids) {
                result.addAll(asteroid.getVehicles());
            }

            //Check that none of the vehicles are destroyed. This should never happen.
            for (int i = result.size()-1; i >= 0; i--) {
                if(getVehicleById(result.get(i)).isDestroyed()){
                    result.remove(i);
                }
            }

            //If all vehicles are targetable, just return all the vehicles
            if(card.getAllowedTarget() == Card.AllowedTarget.ALL){
                return result;
            }
            //If the player is a valid target and the card targets only the player, return a list containing only the player.
            else if(card.getAllowedTarget() == Card.AllowedTarget.PLAYER && result.contains(instigatorID)){
                result = new ArrayList<>();
                result.add(instigatorID);
                return result;
            }
            //TargetType is PLAYER, but the player is not a valid target for the card - return an empty array of valid targets.
            else if(card.getAllowedTarget() == Card.AllowedTarget.PLAYER && !result.contains(instigatorID)){
                result = new ArrayList<>();
                return result;
            }
            //Target cannot be the player, remove the player from the result array.
            else if(card.getAllowedTarget() == Card.AllowedTarget.ENEMY){
                result.remove(instigatorID);
                return result;
            }
        }


        return result;
    }

}
