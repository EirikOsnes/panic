package com.panic.tdt4240.models;

import java.util.List;

/**
 * Created by Hermann on 05.03.2018.
 */

public class Hand {

    private List<Card> cardHand;

    public Hand(){
    }
    public void setCardHand(List<Card> cardHand){
        this.cardHand = cardHand;
    }
    public List<Card> getHand(){
        return cardHand;
    }

    //Placeholder for the Card class, remove when the class exists
    private class Card{}
}
