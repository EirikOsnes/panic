package com.panic.tdt4240.models;

import com.panic.tdt4240.util.GlobalConstants;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Hermann on 09.03.2018.
 */

public class Player {

    private Deck deck;
    private Hand hand;
    private Vehicle vehicle;
    //Any draw cards modifiers
    private int MODIFIED_AMOUNT_CARDS = 0;

    public Player(Stack<Card> cards){
        deck = new Deck(cards);
        hand = new Hand();
        vehicle = new Vehicle();
    }
    //Returns the full deck
    public ArrayList<Card> getCardDeck(){
        return new ArrayList<>(deck.getDeck());
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

}
