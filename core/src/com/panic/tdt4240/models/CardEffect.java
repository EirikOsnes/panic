package com.panic.tdt4240.models;

/**
 * Created by Eirik on 09-Mar-18.
 */

public class CardEffect {

    private String targetStatus;
    private float value;
    private int statusDuration;
    private int splashRange;
    private boolean friendlyFire;
    private String requirementName = "none";
    private float requirementVal = 0;

    CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire) {
        this.targetStatus = targetStatus;
        this.value = value;
        this.statusDuration = statusDuration;
        this.splashRange = splashRange;
        this.friendlyFire = friendlyFire;
    }

    CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire, String requirementName, float requirementVal) {
        new CardEffect(targetStatus,value,statusDuration,splashRange,friendlyFire);
        this.requirementName = requirementName;
        this.requirementVal = requirementVal;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public float getValue() {
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

    public String getRequirementName() {
        return requirementName;
    }

    public float getRequirementVal() {
        return requirementVal;
    }
}