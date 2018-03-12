package com.panic.tdt4240.models;

import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventListener;

/**
 * Created by Eirik on 05-Mar-18.
 */

public class Asteroid implements EventListener{



    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.ATTACK) {
            //TODO: Handle attack event
        }
    }
}
