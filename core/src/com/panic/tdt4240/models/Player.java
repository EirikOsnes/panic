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

public class Player implements EventListener {

    private Deck deck;
    private Hand hand;
    private Vehicle vehicle;
    private boolean alive = true;
    //Any draw cards modifiers
    private int MODIFIED_AMOUNT_CARDS = 0;


    public Player(Stack<Card> cards){
        deck = new Deck(cards);
        hand = new Hand();
        vehicle = new Vehicle();
        EventBus.getInstance().addListener(this);
    }
    public int getAmountDrawnCards(){
        return MODIFIED_AMOUNT_CARDS + GlobalConstants.BASE_DRAW_CARDS;
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
        hand.setCardHand(deck.drawHand(GlobalConstants.BASE_DRAW_CARDS + MODIFIED_AMOUNT_CARDS));
        return hand.getHand();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.DESTROYED && e.getInstigatorID().equals(vehicle.getVehicleID())) {
            alive = false;
            EventBus.getInstance().removeListener(this);
        }
    }
}
