package com.panic.tdt4240.events;

import com.panic.tdt4240.models.Asteroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Choffa for testJava on 25-Feb-18.
 * On permission can be used outside testJava.
 */

public class EventBus {

    private static final EventBus instance = new EventBus();

    public static EventBus getInstance() {
        return instance;
    }

    private ArrayList<EventListener> subs;
    private ArrayList<EventListener> removeList;
    private ArrayList<Event> events;

    private EventBus() {
        this.subs = new ArrayList<>();
        this.events = new ArrayList<>();
        this.removeList = new ArrayList<>();
    }

    public void addListener(EventListener el) {
        subs.add(el);
    }

    public void removeListener(EventListener el) {
        removeList.add(el);
    }

    /**
     * Tell the EventBus that it is safe to remove listeners.
     */
    public void readyForRemove(){
        for (EventListener el : subs) {
            if(el instanceof Asteroid) {
                ((Asteroid) el).readyToRemove();
            }
        }
        subs.removeAll(removeList);
        removeList.clear();
    }

    public void postEvent(Event e) {
        for (EventListener sub : subs) {
            sub.handleEvent(e);
        }
    }

    public void reset(){
        subs = new ArrayList<>();
        removeList = new ArrayList<>();
    }
}
