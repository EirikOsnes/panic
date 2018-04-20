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
    private String missileType = "red";
    private String animationType = "EXPLOSION";

    public CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire) {
        this.targetStatus = targetStatus;
        this.value = value;
        this.statusDuration = statusDuration;
        this.splashRange = splashRange;
        this.friendlyFire = friendlyFire;
    }

    public CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire, String requirementName, float requirementVal) {
        this(targetStatus,value,statusDuration,splashRange,friendlyFire);
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

    public String getMissileType() {
        return missileType;
    }

    public void setMissileType(String missileType) {

        switch (missileType) {
            case "ATTACK":
                this.missileType = "red";
                break;
            case "DEFENCE":
                this.missileType = "none";
                break;
            case "EFFECT":
                this.missileType = "none";
                break;
            default:
                this.missileType = missileType;
                break;
        }
    }

    public String getAnimationType() {
        return animationType;
    }

    public void setAnimationType(String animationType) {

        switch (animationType) {
            case "ATTACK":
                this.animationType = "EXPLOSION";
                break;
            case "DEFENCE":
                this.animationType = "EXPLOSION";
                //TODO: Define this default value
                break;
            case "EFFECT":
                this.animationType = "EXPLOSION";
                //TODO: Define this default value
                break;
            default:
                this.animationType = animationType;
                break;
        }
    }
}