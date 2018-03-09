package com.panic.tdt4240.events;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.CardEffect;

import java.util.ArrayList;

/**
 * Created by Choffa for panic on 09-Mar-18.
 * On permission can be used outside panic.
 */

public class EventFactory {

    /**
     * A factory that creates a list of events connected to the given card
     * @param c             The card to make the events for
     * @param TargetID      The ID of the target of the event
     * @param InstigatorID  The ID of the instigator of the event
     * @return An ArrayList of the Events created from the Card
     */
    public static ArrayList<Event> createEvent(Card c, String TargetID, String InstigatorID) {
        ArrayList<Event> eList = new ArrayList<>();

        for (CardEffect ce : c.getCardEffects()) {
            Event e = new Event(Event.Type.ATTACK, TargetID, InstigatorID);
            e.setDuration(ce.getStatusDuration());
            e.setEffectValue(ce.getValue());
            e.setFriendlyFire(ce.isFriendlyFire());
            e.setStatus(ce.getTargetStatus());
            eList.add(e);
        }
        return eList;
    }
}
