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
     * @param targetID      The ID of the target of the event
     * @param instigatorID  The ID of the instigator of the event
     * @return An ArrayList of the Events created from the Card
     */
    public static ArrayList<Event> createEventFromCard(Card c, String targetID, String instigatorID) {
        //EventFactory.checkIDs(targetID, instigatorID);

        ArrayList<Event> eList = new ArrayList<>();

        for (CardEffect ce : c.getCardEffects()) {
            Event e = new Event(Event.Type.ATTACK, targetID, instigatorID);
            e.setDuration(ce.getStatusDuration());
            e.setEffectValue(ce.getValue());
            e.setFriendlyFire(ce.isFriendlyFire());
            e.setStatus(ce.getTargetStatus());
            eList.add(e);
        }
        return eList;
    }

    public static Event createMoveEvent(String targetID, String instigatorID) {
        // EventFactory.checkIDs(targetID, instigatorID);
        Event e = new Event(Event.Type.MOVE, targetID, instigatorID);
        return e;
    }

    public static Event createDestroyedEvent(String targetID, String instigatorID) {
        // EventFactory.checkIDs(targetID, instigatorID);
        Event e = new Event(Event.Type.DESTROYED, targetID, instigatorID);
        return e;
    }

    static void checkIDs(String ID1, String ID2) {
        if (!ID1.matches("[A-Z]-\\d\\d\\d") || !ID2.matches("[A-Z]-\\d\\d\\d")) {
            throw new IllegalArgumentException("ID should be on format L-DDD where L is a capital letter and D is any digit");
        }
    }
}
