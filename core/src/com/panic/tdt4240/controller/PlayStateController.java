package com.panic.tdt4240.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.panic.tdt4240.models.Hand;
import com.panic.tdt4240.models.Card;

/**
 * Created by victor on 05.03.2018.
 * Changelog:
 *      5. mars: Skisse av metoder.
 *
 */

public class PlayStateController extends AbstractController {

    private boolean cardSelected=false, cardUsed=true;

    public PlayStateController(){
        super();
    }


    // TODO: tilpass til diverse klasser under ulike omstendigheter.
    public void update(Hand hand){
        if (justTouched()){
            getPointerPosition();
            if (isTouching()){ // noe dra-funksjoner?

            }
/*            for (Card c : hand.getHand())
                if (clickedCard()){ //
                    if (cardSelected) {
                        cardSelected=false;
                        cardUsed = true;
                    }
                    cardSelected = true;

                //TODO: gjør noe med kortet, f.eks. la det bli større

                }*/
        }

    }

    //TODO: sikre kompatibilitet med Card-klassen
    private boolean clickedCard(){
        return true;
    }
/**/


}
