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

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void reset(){
        gi = new GameInstance();
    }

    public void playTurn(ArrayList<String[]> playedCards){

        EventFactory.postNewTurnEvent();
        long seed = Long.parseLong(playedCards.get(0)[3]);

        Random random = new Random(seed);
        for (String[] s : playedCards) {
            Card card = ModelHolder.getInstance().getCardById(s[0]);
            ArrayList<String> validTargets = getAllValidTargets(card, s[2]);
            if(validTargets.contains(s[1])){
                EventFactory.postEventsFromCard(card,s[1],s[2]);
            }
            else {
                if(validTargets.size()>0) {
                    int index = random.nextInt(validTargets.size());
                    EventFactory.postEventsFromCard(card,validTargets.get(index),s[2]);
                }

                else{
                    //FIXME: No valid targets - how to make the card go away? For visuals.
                }

            }
        }

        EventFactory.postEndTurnEvent();
    }

    private ArrayList<String> getAllValidTargets(Card card, String instigatorID){
        ArrayList<String> result = new ArrayList<>();

        String startPosition = instigatorID;
        if(instigatorID.charAt(0) == 'A'){
            startPosition = instigatorID;
        }else if(instigatorID.charAt(0)=='V'){
            startPosition = gi.locateVehicle(instigatorID);

            if(startPosition == null){
                return result;
            }
        }

        int[][] adjacency = gi.getMap().getAdjacency();
        int index = Integer.parseInt(startPosition.substring(2))-1;
        int[] neighbours = adjacency[index];

        ArrayList<Asteroid> validAsteroids = new ArrayList<>();
        for (int i = 0; i < neighbours.length; i++) {
            if(neighbours[i]>=card.getMinRange() && neighbours[i]<=card.getMaxRange()){
                validAsteroids.add(gi.getMap().getAsteroids().get(i));
            }
        }

        if(card.getTargetType() == Card.TargetType.ASTEROID){
            for (Asteroid asteroid : validAsteroids) {
                result.add(asteroid.getId());
            }
            return result;
        }

        if(card.getTargetType() == Card.TargetType.VEHICLE){
            for (Asteroid asteroid : validAsteroids) {
                result.addAll(asteroid.getVehicles());
            }

            if(card.getAllowedTarget() == Card.AllowedTarget.ALL){
                return result;
            }
            else if(card.getAllowedTarget() == Card.AllowedTarget.PLAYER && result.contains(instigatorID)){
                result = new ArrayList<>();
                result.add(instigatorID);
                return result;
            }else if(card.getAllowedTarget() == Card.AllowedTarget.ENEMY){
                result.remove(instigatorID);
                return result;
            }
        }


        return result;
    }

}
