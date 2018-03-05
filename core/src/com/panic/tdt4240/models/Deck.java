package com.panic.tdt4240.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Hermann on 05.03.2018.
 */

public class Deck {

    private Stack<Card> cardDeck;
    private Stack<Card> usedCards;

    public Deck(Stack<Card> cardDeck) {
        this.cardDeck = cardDeck;
        this.usedCards = new Stack<Card>();
    }
    public Stack<Card> seeDeck(){
        return cardDeck;
    }

    public List<Card> drawHand(int amount){
        List<Card> hand = new ArrayList<Card>(amount);

        for (int i = 0; i < amount; i++) {
            //Checks if the card deck is empty, merges in usedCards if it is
            if(cardDeck.size() == 0){
                combineDeck();
            }
            //Add card from top of deck to hand, place it in usedCards pile
            Card card = cardDeck.pop();
            hand.add(i,card);
            usedCards.push(card);
        }
        return hand;
    }
    //Shuffle the cardDeck
    private void shuffleDeck(){
        Collections.shuffle(cardDeck);
    }
    //Add the usedCards stack into the cardDeck stack
    private void combineDeck(){
        cardDeck.addAll(usedCards);
        usedCards.clear();
        shuffleDeck();
    }

    //Placeholder for the Card class, remove when the class exists
    private class Card {}
}
