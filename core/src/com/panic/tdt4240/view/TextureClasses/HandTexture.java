package com.panic.tdt4240.view.TextureClasses;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Hand;

import java.util.ArrayList;

/**
 * Created by victor on 09.03.2018.
 */

public class HandTexture {

    private ArrayList<CardDrawable> cardImgs;

    public HandTexture(){
        cardImgs = new ArrayList<>();
    }

    public HandTexture(Hand h){
        cardImgs = new ArrayList<>();
        for (Card c : h.getHand()){
            cardImgs.add(new CardDrawable(c));
        }
    }


    // If anybody feels lazy just slap the card in, although this
    // will be slower than using the card names.
    public void addCard(Card c){
        cardImgs.add(new CardDrawable(c));
    }

    public void addCard(String cardName){
        cardImgs.add(new CardDrawable(cardName));
    }

    public ArrayList<CardDrawable> getCardImgs(){
        return cardImgs;
    }



}
