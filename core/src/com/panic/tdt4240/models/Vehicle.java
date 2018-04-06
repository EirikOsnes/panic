package com.panic.tdt4240.models;

import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.util.StatusHandler;

/**
 * The Object for the Vehicle units.
 */
public class Vehicle implements EventListener {

    private StatusHandler statusHandler;
    private String vehicleID;
    private String vehicleType;

    public Vehicle(String type){
        statusHandler = new StatusHandler(this);
        EventBus.getInstance().addListener(this);
        vehicleType = type;
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;

    }

    public String getVehicleID() {
        return vehicleID;
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.ATTACK) {
            this.statusHandler.addStatusAddition(e.getStatus(), e.getEffectValue(), e.getDuration());
        }
    }
}
