package com.panic.tdt4240.models;

/**
 * Created by Eirik on 09-Mar-18.
 */

public class CardEffect {

    String targetStatus;
    double value;
    int statusDuration;
    int splashRange;
    boolean friendlyFire;

    CardEffect(String targetStatus, double value,int statusDuration, int splashRange, boolean friendlyFire) {
        this.targetStatus = targetStatus;
        this.value = value;
        this.statusDuration = statusDuration;
        this.splashRange = splashRange;
        this.friendlyFire = friendlyFire;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public double getValue() {
        return value;
    }

    public int getStatusDuration() {
        return statusDuration;
    }

    public int getSplashRange() {
        return splashRange;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }
}