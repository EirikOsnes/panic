package com.panic.tdt4240.models;

import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.events.EventListener;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Hermann on 09.03.2018.
 */

public class Player {

    private Deck deck;
    private Hand hand;
    private Vehicle vehicle; //TODO: Make this ID only?
    private boolean alive = true;
    //Any draw cards modifiers
    private int MODIFIED_DRAWN_CARDS = 0;
    //Any play cards modifiers
    private int MODIFIED_PLAYED_CARDS = 0;


    public Player(Stack<Card> cards){
        deck = new Deck(cards);
        hand = new Hand();
        //vehicle = new Vehicle();
    }
    public int getAmountDrawnCards(){
        return MODIFIED_DRAWN_CARDS + GlobalConstants.BASE_DRAW_CARDS;
    }
    public int getAmountPlayedCards(){
        return MODIFIED_PLAYED_CARDS + GlobalConstants.BASE_PLAY_CARDS;
    }
    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    //Returns the full deck
    public ArrayList<Card> getCardDeck(){
        return new ArrayList<>(deck.getFullDeck());
    }
    public ArrayList<Card> getHand(){
        return hand.getHand();
    }
    //Returns the hand
    public ArrayList<Card> playCards(){
        hand.setCardHand(deck.drawHand(getAmountDrawnCards()));
        return hand.getHand();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
