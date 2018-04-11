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

    static EventBus eb = EventBus.getInstance();

    /**
     * A factory that post events connected to the given card. This also creates and posts the
     * card played timing event so it is unnecessary to post this separately
     * @param c             The card to make the events for
     * @param targetID      The ID of the target of the event
     * @param instigatorID  The ID of the instigator of the event
     * @return              A list of the events created with the timing event at the end,
     *                      DO NOT REPOST this list, this is only used for testing
     */
    public static ArrayList<Event> postEventsFromCard(Card c, String targetID, String instigatorID) {
        EventFactory.checkIDs(targetID, instigatorID);

        ArrayList<Event> events = new ArrayList<>();
        for (CardEffect ce : c.getCardEffects()) {
            Event e = new Event(Event.Type.ATTACK, targetID, instigatorID);
            e.setDuration(ce.getStatusDuration());
            e.setEffectValue(ce.getValue());
            e.setFriendlyFire(ce.isFriendlyFire());
            e.setStatus(ce.getTargetStatus());
            e.setRequirementName(ce.getRequirementName());
            e.setRequirementVal(ce.getRequirementVal());
            eb.postEvent(e);
            events.add(e);
        }
        Event e = new Event(Event.Type.TIMING, targetID, instigatorID);
        e.setTiming(StatusHandler.TimingType.CARD_PLAYED);
        eb.postEvent(e);
        return events;
    }

    /**
     * A factory that creates and posts move events, this should be called each time
     * a vehicle wants to move from one asteroid to another. Or if you want to move another
     * vehicle
     * @param targetID          The ID of the asteroid the vehicle should be moved to
     * @param instigatorID      The ID of the vehicle to be moved
     * @return                  The Event that was posted, DO NOT REPOST, this is only used for testing
     */
    public static Event postMoveEvent(String targetID, String instigatorID) {
        EventFactory.checkIDs(targetID, instigatorID);
        Event e = new Event(Event.Type.MOVE, targetID, instigatorID);
        eb.postEvent(e);
        return e;
    }

    /**
     * A factory that creates and posts destroyed events, this indicates that a vehicle
     * or an asteroid has been destroyed
     * @param targetID          The ID of the object destroyed
     * @param instigatorID      The ID of the destroyer if applicable
     * @return                  The Event that was posted, DO NOT REPOST, this is only used for testing
     */
    public static Event postDestroyedEvent(String targetID, String instigatorID) {
        EventFactory.checkIDs(targetID, instigatorID);
        Event e = new Event(Event.Type.DESTROYED, targetID, instigatorID);
        eb.postEvent(e);
        return e;
    }

    static void checkIDs(String ID1, String ID2) {
        if (!ID1.matches("[A-Z]-\\d\\d\\d") || !ID2.matches("[A-Z]-\\d\\d\\d")) {
            throw new IllegalArgumentException("ID should be on format L-DDD where L is a capital letter and D is any digit");
        }
    }

    /**
     * A factory that creates and posts a turn start timing event that should be done at the
     * start of each turn
     * @return      The Event that was posted, DO NOT REPOST, this is only used for testing
     */
    public static Event postNewTurnEvent() {
        Event e = new Event(Event.Type.TIMING, "", "");
        e.setTiming(StatusHandler.TimingType.TURN_START);
        eb.postEvent(e);
        return e;
    }

    /**
     * A factory that creates and posts a turn end timing event that should be done at the end
     * of each turn
     * @return      The Event that was posted, DO NOT REPOST, this is only used for testing
     */
    public static Event postEndTurnEvent() {
        Event e = new Event(Event.Type.TIMING, "", "");
        e.setTiming(StatusHandler.TimingType.TURN_END);
        eb.postEvent(e);
        return e;
    }

    /**
     * Clones and posts a new event with a new target, used when deferring events from
     * an asteroid to a vehicle
     * @param e             The event to clone
     * @param targetID      The ID of the new target
     */
    public static void postClonedEvent(Event e, String targetID) {
        Event clone = e.cloneEvent(targetID);
        eb.postEvent(clone);
    }

    /**
     * Static method for setting the EventBus. Only used for testing with a mock
     * EventBus.
     * @param eb    The EventBus the factory should use
     */
    static void setEventBus(EventBus eb) {
        EventFactory.eb = eb;
    }
}
