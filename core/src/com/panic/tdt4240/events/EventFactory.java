package com.panic.tdt4240.events;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.CardEffect;
import com.panic.tdt4240.util.StatusHandler;

import java.util.ArrayList;

/**
 * Created by Choffa for panic on 09-Mar-18.
 * On permission can be used outside panic.
 */

public class EventFactory {

    /**
     * A factory that post events connected to the given card
     * @param c             The card to make the events for
     * @param targetID      The ID of the target of the event
     * @param instigatorID  The ID of the instigator of the event
     * @param multiplier    An (optional) effect value multiplier
     */
    public static void postEventsFromCard(Card c, String targetID, String instigatorID, float multiplier) {
        //EventFactory.checkIDs(targetID, instigatorID);

        for (CardEffect ce : c.getCardEffects()) {
            Event e = new Event(Event.Type.ATTACK, targetID, instigatorID);
            e.setDuration(ce.getStatusDuration());
            e.setEffectValue(ce.getValue()*multiplier);
            e.setFriendlyFire(ce.isFriendlyFire());
            e.setStatus(ce.getTargetStatus());
            e.setRequirementName(ce.getRequirementName());
            e.setRequirementVal(ce.getRequirementVal());
            EventBus.getInstance().postEvent(e);

        }
        Event e = new Event(Event.Type.TIMING, targetID, instigatorID);
        e.setTiming(StatusHandler.TimingType.CARD_PLAYED);
        EventBus.getInstance().postEvent(e);
    }

    /**
     * A factory that post events connected to the given card
     * @param c             The card to make the events for
     * @param targetID      The ID of the target of the event
     * @param instigatorID  The ID of the instigator of the event
     */
    public static void postEventsFromCard(Card c, String targetID, String instigatorID){
        postEventsFromCard(c,targetID,instigatorID,1);
    }

    public static Event createMoveEvent(String targetID, String instigatorID) {
        // EventFactory.checkIDs(targetID, instigatorID);
        return new Event(Event.Type.MOVE, targetID, instigatorID);
    }

    public static Event createDestroyedEvent(String targetID, String instigatorID) {
        // EventFactory.checkIDs(targetID, instigatorID);
        return new Event(Event.Type.DESTROYED, targetID, instigatorID);
    }

    static void checkIDs(String ID1, String ID2) {
        if (!ID1.matches("[A-Z]-\\d\\d\\d") || !ID2.matches("[A-Z]-\\d\\d\\d")) {
            throw new IllegalArgumentException("ID should be on format L-DDD where L is a capital letter and D is any digit");
        }
    }

    public static void postNewTurnEvent() {
        Event e = new Event(Event.Type.TIMING, "", "");
        e.setTiming(StatusHandler.TimingType.TURN_START);
        EventBus.getInstance().postEvent(e);
    }

    public static void postEndTurnEvent() {
        Event e = new Event(Event.Type.TIMING, "", "");
        e.setTiming(StatusHandler.TimingType.TURN_END);
        EventBus.getInstance().postEvent(e);
    }
}
