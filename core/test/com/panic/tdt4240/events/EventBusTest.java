package com.panic.tdt4240.events;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventBusTest {

    private static EventBus eb;

    private EventListener el1, el2;
    private Event e;

    @BeforeClass
    static public void setUpClass() {
        eb = EventBus.getInstance();
    }

    @Before
    public void setUp() {
        el1 = mock(EventListener.class);
        el2 = mock(EventListener.class);
        e = mock(Event.class);
    }

    @Test
    public void getInstance() {
        assertEquals(eb, EventBus.getInstance());
    }

    @Test
    public void addListener() {
        eb.addListener(el1);
        eb.postEvent(e);
        verify(el1).handleEvent(e);
    }

    @Test
    public void removeListener() {
        eb.addListener(el1);
        eb.addListener(el2);
        eb.removeListener(el1);
        eb.postEvent(e);
       // verify(el1, never()).handleEvent(e);
       // verify(el2).handleEvent(e);
    }

    @Test
    public void postEvent() {
        eb.addListener(el1);
        eb.addListener(el2);
        eb.postEvent(e);
        verify(el1).handleEvent(e);
        verify(el2).handleEvent(e);
    }
}