package com.panic.tdt4240.models;

import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventBus;
import com.panic.tdt4240.events.EventFactory;

import java.util.ArrayList;

/**
 * Created by Eirik on 05-Mar-18.
 */

public class Card {

    public enum CardType {ATTACK, EFFECT, DEFENCE, MOVEMENT}
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

    //region Getters/Setters
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

    public ArrayList<CardEffect> getCardEffects() {
        return cardEffects;
    }

    //endregion

    public void addCardEffect(String targetStatus, float value, int statusDuration, int splashRange, boolean friendlyFire){
        cardEffects.add(new CardEffect(targetStatus, value, statusDuration, splashRange, friendlyFire));
    }

    public void addCardEffect(String targetStatus, float value, int statusDuration, int splashRange, boolean friendlyFire, String requirementName, float requirementValue){
        cardEffects.add(new CardEffect(targetStatus, value, statusDuration, splashRange, friendlyFire, requirementName, requirementValue));
    }

    public void addCardEffect(CardEffect ce){
        cardEffects.add(ce);
    }

    public void playCard(String targetID, String instigatorID) {
        if (cardType == CardType.MOVEMENT) {
            EventFactory.postMoveEvent(targetID, instigatorID);
        }
        else if (cardType == CardType.ATTACK) {
            EventFactory.postEventsFromCard(this, targetID, instigatorID);
        } else{
            //FIXME: Should this be specified further?
            EventFactory.postEventsFromCard(this, targetID, instigatorID);
        }
    }
}
