package com.panic.tdt4240.models;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Hermann on 09.03.2018.
 */

public class TestDeck {

    private Deck deck;
    private Stack<Card> cardList;
    private int cardAmount = 4;

    @Before
    public void init(){
        cardList = new Stack<>();
        for (int i = 0; i < cardAmount; i++) {
            cardList.push(new Card(cardAmount-i + ""));
        }
        deck = new Deck(cardList);
    }

    @Test
    public void testReturnDecks(){
        assertEquals(cardList, deck.getCardDeck());
        assertTrue(deck.getUsedCards().empty());
    }

    @Test
    public void drawMoreThanDeck(){
        int handSize = cardAmount*2;
        List<Card> hand = deck.drawHand(handSize);
        assertTrue(hand.size() == handSize);
        assertTrue(deck.getFullDeck().size() == cardAmount);
    }

    @Test
    public void drawLessThanDeck(){
        int handSize = cardAmount -1;
        List<Card> hand = deck.drawHand(handSize);
        assertFalse(hand.contains(deck.getCardDeck().peek()));
        assertTrue(deck.getCardDeck().size() == 1);
    }

}
