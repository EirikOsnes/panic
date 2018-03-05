package com.panic.tdt4240.events;

import java.util.ArrayList;

/**
 * Created by Choffa for testJava on 25-Feb-18.
 * On permission can be used outside testJava.
 */

class EventBus {

    private static final EventBus instance = new EventBus();

    static EventBus getInstance() {
        return instance;
    }

    private ArrayList<EventListener> subs;

    private EventBus() {
        this.subs = new ArrayList<>();
    }

    public void addListener(EventListener el) {
        subs.add(el);
    }

    public void removeListener(EventListener el) {
        subs.remove(el);
    }

    public void postEvent(Event e) {
        for (EventListener el : subs) {
            el.handleEvent(e);
        }
    }
}
