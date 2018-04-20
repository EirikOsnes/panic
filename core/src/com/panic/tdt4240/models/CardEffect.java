package com.panic.tdt4240.models;

import com.panic.tdt4240.view.animations.CloudAnimation.AnimationType;
import com.panic.tdt4240.view.animations.Missile.MissileType;

/**
 * Created by Eirik on 09-Mar-18.
 */

public class CardEffect {

    private String targetStatus;
    private float value;
    private int statusDuration;
    private int splashRange;
    private boolean friendlyFire;
    private String requirementName;
    private float requirementVal;
    private MissileType missileType;
    private AnimationType animationType;

    public CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire) {
        this(targetStatus, value, statusDuration, splashRange, friendlyFire, "none", 0);
    }

    public CardEffect(String targetStatus, float value,int statusDuration, int splashRange, boolean friendlyFire, String requirementName, float requirementVal) {
        this.targetStatus = targetStatus;
        this.value = value;
        this.statusDuration = statusDuration;
        this.splashRange = splashRange;
        this.friendlyFire = friendlyFire;
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

    public MissileType getMissileType() {
        return missileType;
    }

    public void setMissileType(MissileType missileType) {
        this.missileType = missileType;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }
}