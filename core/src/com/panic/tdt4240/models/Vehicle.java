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

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public Vehicle cloneVehicleWithId(String id) {
        Vehicle v = new Vehicle(this.vehicleType);

        for (String s : statusHandler.getAllResultants().keySet()) {
            v.statusHandler.addStatus(s, statusHandler.getStatusResultant(s));
        }

        v.vehicleID = id;
        return v;
    }

    @Override
    public String toString() {
        return "Vehicle with id: " + vehicleID + "\n" + statusHandler.toString();
    }

    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.ATTACK && e.getTargetID() == this.vehicleID) {
            this.statusHandler.addStatusAddition(e.getStatus(), e.getEffectValue(), e.getDuration());
        }
    }
}
