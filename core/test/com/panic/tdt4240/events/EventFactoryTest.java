package com.panic.tdt4240.events;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.CardEffect;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Choffa for panic on 09-Mar-18.
 * On permission can be used outside panic.
 */
public class EventFactoryTest {

    private Card c;
    private CardEffect ce;
    private static final int DURATION = 972;
    private static final float VALUE = 3.14159265f;
    private static final boolean FRIENDLY = false;
    private static final String STATUS = "STATUSSTRING", TARGET = "T-129", INSTIGATOR = "I-456";

    @Before
    public void init() {
        return;
    }

/*    @Test
    public void testCreateEventFromCard() {
        c = mock(Card.class);
        ce = mock(CardEffect.class);
        when(ce.getStatusDuration()).thenReturn(DURATION);
        when(ce.getValue()).thenReturn(VALUE);
        when(ce.isFriendlyFire()).thenReturn(FRIENDLY);
        when(ce.getTargetStatus()).thenReturn(STATUS);
        ArrayList<CardEffect> ceList = new ArrayList<>();
        ceList.add(ce);
        ceList.add(ce);
        when(c.getCardEffects()).thenReturn(ceList);

        ArrayList<Event> events = EventFactory.createEventFromCard(c, TARGET, INSTIGATOR);
        for (Event e : events) {
            assertEquals(TARGET, e.getTargetID());
            assertEquals(INSTIGATOR, e.getInstigatorID());
            assertEquals(DURATION, e.getDuration());
            assertEquals(VALUE, e.getEffectValue(), 0.0001);
            assertEquals(FRIENDLY, e.isFriendlyFire());
            assertEquals(STATUS, e.getStatus());
        }
    }*/

    @Test
    public void testCreateMoveEvent() {
        Event e = EventFactory.createMoveEvent(TARGET, INSTIGATOR);
        assertEquals(Event.Type.MOVE, e.getT());
        assertEquals(TARGET, e.getTargetID());
        assertEquals(INSTIGATOR, e.getInstigatorID());
    }

    @Test
    public void testCreateDestroyedEvent() {
        Event e = EventFactory.createDestroyedEvent(TARGET, INSTIGATOR);
        assertEquals(Event.Type.DESTROYED, e.getT());
        assertEquals(TARGET, e.getTargetID());
        assertEquals(INSTIGATOR, e.getInstigatorID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIDs() {
        try {
            EventFactory.checkIDs(TARGET, INSTIGATOR);
        } catch (Exception e){
            fail("IDs: " + TARGET + " or " + INSTIGATOR + " caused test to fail");
        }
        EventFactory.checkIDs("HELLO", "WORLD");
    }
}