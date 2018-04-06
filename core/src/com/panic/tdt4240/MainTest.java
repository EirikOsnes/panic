package com.panic.tdt4240;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.StatusHandler;

import java.util.Stack;

/**
 * Created by Choffa for panic on 06-Apr-18.
 * On permission can be used outside panic.
 * Not actual game code but rather a proof of concept
 */
final class MainTest {

    private static ModelHolder mh;

    public static void main(String[] args) {
        mh = ModelHolder.getInstance();
        Vehicle myVehicle = mh.getVehicleByName("EDDISON").cloneVehicleWithId("V-001");
        Vehicle myInstigator = mh.getVehicleByName("EDDISON").cloneVehicleWithId("V-002");
        Map myMap = mh.getMap();
        Stack<Card> cardStack = new Stack<>();
        cardStack.addAll(mh.getAllCards());
        Player myPlayer = new Player(cardStack);
        myMap.addVehicle("RED", myVehicle);
        myMap.getAsteroids().get(0).addVehicle(myVehicle.getVehicleID());
        myMap.addVehicle("GREEN", myInstigator);
        myMap.getAsteroids().get(1).addVehicle(myInstigator.getVehicleID());

        System.out.println(myVehicle.toString());
        System.out.println(myInstigator.toString());

        //EventFactory.postNewTurnEvent();
        ModelHolder.getInstance().getCardById("PEW").playCard(myMap.getAsteroids().get(0).getId(), myInstigator.getVehicleID());
        myVehicle.getStatusHandler().runEffects(StatusHandler.TimingType.CARD_PLAYED);
        myInstigator.getStatusHandler().runEffects(StatusHandler.TimingType.CARD_PLAYED);
        myVehicle.getStatusHandler().runEffects(StatusHandler.TimingType.TURN_END);
        myInstigator.getStatusHandler().runEffects(StatusHandler.TimingType.TURN_END);
        //EventFactory.postEndTurnEvent();


        System.out.println(myVehicle.toString());
        System.out.println(myInstigator.toString());
    }
}
