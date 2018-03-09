package com.panic.tdt4240.events;

/**
 * Created by Choffa for testJava on 25-Feb-18.
 * On permission can be used outside testJava.
 */

class Event {
    /**
     * An enum that defines the type of event that occurred
     */
    public enum Type {ATTACK, DESTROYED, MOVE}

    private Type t;
    private int instigatorID, targetID;
    private String status;
    private int duration;
    private double effectValue;
    private boolean frendlyFire;

    public Event(Type t, int targetID, int instigatorID) {
        this.t = t;
        this.instigatorID = instigatorID;
        this.targetID = targetID;
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

    public boolean isFrendlyFire() {
        return frendlyFire;
    }

    public Type getT() {
        return t;
    }

    public int getInstigatorID() {
        return instigatorID;
    }

    public int getTargetID() {
        return targetID;
    }

    void setT(Type t) {
        this.t = t;
    }

    void setInstigatorID(int instigatorID) {
        this.instigatorID = instigatorID;
    }

    void setTargetID(int targetID) {
        this.targetID = targetID;
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

    void setFrendlyFire(boolean frendlyFire) {
        this.frendlyFire = frendlyFire;
    }
}
