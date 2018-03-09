package com.panic.tdt4240.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Hermann on 09.03.2018.
 */

public class Player {

    private Deck deck;
    private Hand hand;
    private Vehicle vehicle;
    //Base number of cards drawn each round, and modifiers to this
    private final int BASE_AMOUNT_CARDS = 5;
    private int MODIFIED_AMOUNT_CARDS = 0;

    public Player(Stack<Card> cards){
        deck = new Deck(cards);
        hand = new Hand();
        vehicle = new Vehicle();
    }
    public ArrayList<Card> getCardDeck(){
        return new ArrayList<>(deck.getDeck());
    }
    public ArrayList<Card> playCards(){
        hand.setCardHand(deck.drawHand(BASE_AMOUNT_CARDS + MODIFIED_AMOUNT_CARDS));
        return hand.getHand();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }



}
