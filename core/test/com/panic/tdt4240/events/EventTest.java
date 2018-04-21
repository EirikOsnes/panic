package com.panic.tdt4240.events;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Choffa for panic on 09-Mar-18.
 * On permission can be used outside panic.
 */

public class EventTest {

    private Event event;
    private static final String TARGETID = "TARGET", INSTIGATORID = "INSTIGATOR", STATUS = "STATUS";
    private static final int DURATION = 10;
    private static final float VALUE = 9.9999f;


    @Before
    public void init() {
        event = new Event(Event.Type.ATTACK, TARGETID, INSTIGATORID);
    }

    @Test
    public void testStatus() {
        Assert.assertNull("Failed before setting status", event.getStatus());
        event.setStatus(STATUS);
        Assert.assertEquals("Failed after setting status", STATUS, event.getStatus());
    }

    @Test
    public void testDuration() {
        Assert.assertEquals("Failed before setting duration", event.getDuration(), 0);
        event.setDuration(DURATION);
        Assert.assertEquals("Failed after setting duration", DURATION, event.getDuration());
    }

    @Test
    public void testID () {
        Assert.assertEquals("Failed to get Target", TARGETID, event.getTargetID());
        Assert.assertEquals("Failed to get instigator", INSTIGATORID, event.getInstigatorID());
    }

    @Test
    public void testEffectValue() {
        Assert.assertEquals("Failed before setting effectValue", event.getEffectValue(), 0.0f, 0.00001f);
        event.setEffectValue(VALUE);
        Assert.assertEquals("Failed after setting effectValue", event.getEffectValue(), VALUE, 0.00001f);
    }

    @Test
    public void testFriendlyFire() {
        Assert.assertFalse("Failed before setting friendlyFire", event.isFriendlyFire());
        event.setFriendlyFire(true);
        Assert.assertTrue("Failed after setting friendlyFire", event.isFriendlyFire());
    }

    @Test
    public void testType() {
        Assert.assertEquals(Event.Type.ATTACK, event.getT());
        event.setT(Event.Type.DESTROYED);
        Assert.assertEquals(Event.Type.DESTROYED, event.getT());
        event.setT(Event.Type.MOVE);
        Assert.assertEquals(Event.Type.MOVE, event.getT());
    }


}

