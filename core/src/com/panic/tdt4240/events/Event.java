package com.panic.tdt4240.events;

import com.panic.tdt4240.util.StatusHandler;
import com.panic.tdt4240.view.animations.CloudAnimation;
import com.panic.tdt4240.view.animations.CloudAnimation.AnimationType;
import com.panic.tdt4240.view.animations.Missile;
import com.panic.tdt4240.view.animations.Missile.MissileType;

/**
 * This is a class that represents the Events in the game panic
 * project for TDT4240
 * Created by Choffa
 */

public class Event {


    /**
     * An enum that defines the type of event that occurred
     */
    public enum Type {ATTACK, DESTROYED, MOVE, TIMING}

    private Type t;
    private MissileType missileType;
    private AnimationType cloudType;
    private StatusHandler.TimingType tt;
    private String instigatorID, targetID;
    private String status, requirementName;
    private int duration, splashRange;
    private float effectValue, requirementVal;
    private boolean friendlyFire, splashDamage;


    Event(Type t, String targetID, String instigatorID) {
        this.t = t;
        this.targetID = targetID;
        this.instigatorID = instigatorID;
        this.missileType = MissileType.NONE;
        this.cloudType = AnimationType.NONE;
    }

    public String getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public float getEffectValue() {
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

    public StatusHandler.TimingType getTiming() {
        return tt;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public float getRequirementVal() {
        return requirementVal;
    }

    public int getSplashRange() {
        return splashRange;
    }

    public boolean isSplashDamage() {
        return splashDamage;
    }

    public MissileType getMissileType() {
        return missileType;
    }

    public AnimationType getCloudType() {
        return cloudType;
    }

    void setMissileType(MissileType missileType) {
        this.missileType = missileType;
    }

    void setCloudType(AnimationType cloudType) {
        this.cloudType = cloudType;
    }

    void setSplashDamage(boolean splashDamage) {
        this.splashDamage = splashDamage;
    }

    void setSplashRange(int splashRange) {
        this.splashRange = splashRange;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    void setEffectValue(float effectValue) {
        this.effectValue = effectValue;
    }

    void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    void setT(Type t) {
        this.t = t;
    }

    void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    void setRequirementVal(float requirementVal) {
        this.requirementVal = requirementVal;
    }
      
    void setTiming(StatusHandler.TimingType tt) {
        this.tt = tt;
    }

    /**
     * Clones the event giving it a new target. Used when defering events from
     * asteroids to vehicles
     * @param targetID  The ID of the new target
     * @return          The new event
     */  
    public Event cloneEvent(String targetID) {
        Event e = new Event(this.t, targetID, this.instigatorID);
        e.setStatus(this.status);
        e.setFriendlyFire(this.friendlyFire);
        e.setEffectValue(this.effectValue);
        e.setDuration(this.duration);
        e.setRequirementName(this.requirementName);
        e.setRequirementVal(this.requirementVal);
        e.setSplashDamage(false);
        e.setSplashRange(0);
        //e.setCloudType(this.cloudType);
        return e;
    }
}
