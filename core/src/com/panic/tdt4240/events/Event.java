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
    private double damage = 0.0;

    public Event(Type t, int instigatorID, int targetID) {
        this.t = t;
        this.instigatorID = instigatorID;
        this.targetID = targetID;
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

    public double getDamage() {
        return damage;
    }
}
