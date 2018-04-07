package com.panic.tdt4240.states;

import com.panic.tdt4240.events.EventFactory;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameModelHolder;
import com.panic.tdt4240.models.ModelHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * State for running thorught the cards gotten from the server.
 */

public class RunEffectsState extends State {

    GameModelHolder gmh;

    protected RunEffectsState(GameStateManager gsm) {
        super(gsm);
        gmh = GameModelHolder.getInstance();
    }

    private void playAllCards(){
        ArrayList<String[]> playedCards; //TODO get these from server
        long seed; //TODO get this from server

        //Until we have server:
        playedCards = new ArrayList<>();
        seed = 0;


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
    }

    private ArrayList<String> getAllValidTargets(Card card, String instigatorID){
        ArrayList<String> result = new ArrayList<>();

        String startPosition;
        if(instigatorID.charAt(0) == 'A'){
            startPosition = instigatorID;
        }else if(instigatorID.charAt(0)=='V'){
            startPosition = gmh.locateVehicle(instigatorID);

            if(startPosition == null){
                return result;
            }
        }

        int[][] adjecency = gmh.getMap().getAdjacency();
        int index = Integer.parseInt(instigatorID.substring(2))-1;
        int[] neighbours = adjecency[index];

        ArrayList<Asteroid> validAsteroids = new ArrayList<>();
        for (int i = 0; i < neighbours.length; i++) {
            if(neighbours[i]>=card.getMinRange() && neighbours[i]<=card.getMaxRange()){
                validAsteroids.add(gmh.getMap().getAsteroids().get(i));
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

    @Override
    public void handleInput(Object o) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
