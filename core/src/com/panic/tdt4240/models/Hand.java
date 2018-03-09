package com.panic.tdt4240.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermann on 05.03.2018.
 */

public class Hand {

    private ArrayList<Card> cardHand;

    public Hand(){
    }
    public void setCardHand(ArrayList<Card> cardHand){
        this.cardHand = cardHand;
    }
    public ArrayList<Card> getHand(){
        return cardHand;
    }

}
