package com.panic.tdt4240.events;

/**
 * This is a class that represents the Events in the game panic
 * project for TDT4240
 * Created by Choffa
 */

class Event {
    /**
     * An enum that defines the type of event that occurred
     */
    public enum Type {ATTACK, DESTROYED, MOVE}

    private Type t;
    private String instigatorID, targetID;
    private String status;
    private int duration;
    private double effectValue;
    private boolean friendlyFire;

    public Event(Type t, String targetID, String instigatorID) {
        this.t = t;
        this.targetID = targetID;
        this.instigatorID = instigatorID;
    }

    public String getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public double getEffectValue() {
        return effectValue;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public Type getT() {
        return t;
    }

    public String getInstigatorID() {
        return instigatorID;
    }

    public String getTargetID() {
        return targetID;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    void setEffectValue(double effectValue) {
        this.effectValue = effectValue;
    }

    void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    void setT(Type t) {
        this.t = t;
    }
}
