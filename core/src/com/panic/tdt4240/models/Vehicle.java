package com.panic.tdt4240.models;

import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventFactory;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.util.IStatusAble;
import com.panic.tdt4240.util.StatusHandler;

/**
 * The Object for the Vehicle units.
 */
public class Vehicle implements EventListener,IStatusAble {

    private StatusHandler statusHandler;
    private String vehicleID;
    private String vehicleType;
    //From cars/cars.atlas, ie: red_car, green_car etc
    private String carColor;
    private boolean isDestroyed = false;

    public Vehicle(String type){
        statusHandler = new StatusHandler(this);
        vehicleType = type;
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;

    }
    public String getCarColor(){
        return carColor;
    }
    public void setCarColor(String carColor){
        this.carColor = carColor;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Vehicle cloneVehicleWithId(String id) {
        Vehicle v = new Vehicle(this.vehicleType);

        for (String s : statusHandler.getAllResultants().keySet()) {
            v.statusHandler.addStatus(s, statusHandler.getStatusResultant(s));
        }

        v.vehicleID = id;
        EventBus.getInstance().addListener(v);
        return v;
    }

    @Override
    public String toString() {
        return "Vehicle with id: " + vehicleID + "\n" + statusHandler.toString();
    }

    @Override
    public void handleEvent(Event e) {
        if(!isDestroyed) {
            if (e.getT() == Event.Type.ATTACK && e.getTargetID().equals(this.vehicleID)) {
                if (this.statusHandler.isRequirementsMet(e.getRequirementName(), e.getRequirementVal())) {
                    this.statusHandler.addStatusAddition(e.getStatus(), e.getEffectValue(), e.getDuration());
                }
            }
            if (e.getT() == Event.Type.TIMING) {
                this.statusHandler.runEffects(e.getTiming());
            }
        }
    }

    public void destroy(){
        if(!isDestroyed) {
            isDestroyed = true;
            EventFactory.postDestroyedEvent(vehicleID, vehicleID);
            EventBus.getInstance().removeListener(this);
        }
    }

    public String getSpriteName() {
        return (vehicleType + "_" + carColor).toLowerCase();
    }
}
