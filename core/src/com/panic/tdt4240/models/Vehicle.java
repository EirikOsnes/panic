package com.panic.tdt4240.models;


import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.util.StatusHandler;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Object for the Vehicle units.
 */
public class Vehicle implements EventListener {

    private StatusHandler statusHandler;

    public Vehicle(){
        statusHandler = new StatusHandler(this);
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;

    }
  
    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.MOVE) {
            //TODO: Handle move event
        }
        else if (e.getT() == Event.Type.ATTACK) {
            //TODO: Handle attack event
        }
}
