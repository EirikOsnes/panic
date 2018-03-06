package com.panic.tdt4240.models;

import java.util.ArrayList;

/**
 * Created by Eirik on 05-Mar-18.
 */

public class Card {

    public enum CardType {ATTACK, EFFECT, DEFENSE, MOVEMENT}
    public enum TargetType {ASTEROID, VEHICLE}
    public enum AllowedTarget {PLAYER, ENEMY, ALL}

    private String id;
    private String name;
    private CardType cardType;
    private TargetType targetType;
    private AllowedTarget allowedTarget;
    private int priority;
    private int minRange;
    private int maxRange;
    private String tooltip;
    private ArrayList<CardEffect> cardEffects;

    public Card(String ID){
        this.id = ID;
        cardEffects = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public AllowedTarget getAllowedTarget() {
        return allowedTarget;
    }

    public void setAllowedTarget(AllowedTarget allowedTarget) {
        this.allowedTarget = allowedTarget;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMinRange() {
        return minRange;
    }

    public void setMinRange(int minRange) {
        this.minRange = minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void addCardEffect(String targetStatus, int statusDuration, int splashRange, boolean friendlyFire){
        cardEffects.add(new CardEffect(targetStatus, statusDuration, splashRange, friendlyFire));
    }

    private class CardEffect {

        String targetStatus;
        int statusDuration;
        int splashRange;
        boolean friendlyFire;

        CardEffect(String targetStatus, int statusDuration, int splashRange, boolean friendlyFire) {
            this.targetStatus = targetStatus;
            this.statusDuration = statusDuration;
            this.splashRange = splashRange;
            this.friendlyFire = friendlyFire;
        }
    }
}
