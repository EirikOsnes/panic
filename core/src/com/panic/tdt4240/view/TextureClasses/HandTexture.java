package com.panic.tdt4240.view.TextureClasses;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Hand;
import com.panic.tdt4240.view.ViewClasses.AbstractView;

import java.util.ArrayList;

/**
 * Created by victor on 09.03.2018.
 */

public class HandTexture {

    private ArrayList<CardTexture> cardImgs;

    public HandTexture(){
        cardImgs = new ArrayList<>();
    }

    public HandTexture(Hand h){
        cardImgs = new ArrayList<>();
        for (Card c : h.getHand()){
            cardImgs.add(new CardTexture(c));
        }
    }


    // If anybody feels lazy just slap the card in, although this
    // will be slower than using the card names.
    public void addCard(Card c){
        cardImgs.add(new CardTexture(c));
    }

    public void addCard(String cardName){
        cardImgs.add(new CardTexture(cardName));
    }

    public ArrayList<CardTexture> getCardImgs(){
        return cardImgs;
    }



}
