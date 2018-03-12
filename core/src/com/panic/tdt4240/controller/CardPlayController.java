package com.panic.tdt4240.controller;

import com.panic.tdt4240.models.Card;

/**
 * Created by victor on 05.03.2018.
 * Changelog:
 *      5. mars: Skisse av metoder.
 *
 */

public class CardPlayController extends AbstractController {


    public CardPlayController(){
        super();
    }

    //TODO: implement
    public void selectTarget(){

    }

    public void clickOptions(){

    }


    public void clickCard(PlaceholderCard ph){
        if (ph.isUsed()){
            undoUseCard(ph);
        }
    }

    // player turn ended
    public void lockInCards(){

    }


    // TODO: tilpass til diverse klasser under ulike omstendigheter.
    private void useCard(){
        //
    }

    private void hoverCard(){

    }

    private void undoUseCard(PlaceholderCard ph){
        ph.setIsUsed(false);
        ph.setIsUsed(false);
    }


    public class PlaceholderCard{
        private boolean used;
        private boolean clicked;

        PlaceholderCard(){
            clicked = false;
            used = false;
        }

        public boolean isUsed(){return used;}
        public boolean isClicked(){return clicked;}

        public void setIsUsed(boolean b) { used = b; }
        public void setIsClicked(boolean b) { clicked = b; }
    }

/**/


}
