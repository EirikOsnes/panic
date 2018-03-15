package com.panic.tdt4240.util;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class to hold all statuses and the logic on them.
 */

public class StatusHandler {


    private HashMap<String, Status> statuses;
    //TODO: Potential for bugs from different orderings in the HashMap?
    private ArrayList<String> baseStats;
    private Object parent;
    public enum TIMING_TYPE{TURN_START, CARD_PLAYED ,TURN_END}


    /**
     * A class to hold all statuses and the logic on them. Passes in the parent for now, in case we want some events to trigger from here.
     * @param parent The object that called this class.
     */
    public StatusHandler(Object parent){
        this.parent = parent;
        statuses = new HashMap<>();
        baseStats = new ArrayList<>();
        setupBaseStatuses();
    }

    void setupBaseStatuses(){
        baseStats.add("health");
        baseStats.add("damage_modifier");
        baseStats.add("defence_modifier");
        baseStats.add("movement_modifier");
        baseStats.add("max_damage");

        for (String stat : baseStats) {
            addStatus(stat);
        }
    }

    /**
     * Gets the total resultant for the given status.
     * @param statusName The name of the status.
     * @return The total resultant as a function of baseValue and modifiers. Returns 0 if the status is uninitiated.
     */
    public float getStatusResultant(String statusName){

        if (!statuses.containsKey(statusName)){
            return 0;
        }

        return statuses.get(statusName).getResultant();
    }

    /**
     * Returns a HashMap of all the base stats.
     * @return Returns a HashMap of all the base stats.
     */
    public HashMap<String, Float> getBaseStats(){
        HashMap<String, Float> result = new HashMap<>();
        for (String key : baseStats) {
            result.put(key,getStatusResultant(key));
        }
        return result;
    }

    /**
     * Get the damage_modifier base stat - use whenever initiating an attack.
     * @return Returns the damage_modifier base stat.
     */
    public float getDamageModifier(){
        return getStatusResultant("damage_modifier");
    }

    /**
     * Get the movement_modifier base stat, already rounded to an integer - use whenever initiating a move with this as instigator.
     * @return Returns the damage modifier base stat.
     */
    public int getMovementModifier(){
        return Math.round(getStatusResultant("movement_modifier"));
    }

    /**
     * Should ONLY be used when initiating an object from XML file - or if you wish to override an existing status (as it will be wiped).
     * ONLY use if you know what you are doing.
     * @param name The name of the status
     * @param baseValue The base value of the status.
     */
    public void addStatus(String name, float baseValue){
        statuses.put(name, new Status(name,baseValue));
    }

    /**
     * Should ONLY be used when initiating an object from XML file - or if you wish to override an existing status (as it will be wiped).
     * ONLY use if you know what you are doing.
     * @param name The name of the status
     */
    public void addStatus(String name){
        addStatus(name, StatusConstants.STATUS_VALUES.valueOf(name).getBaseValue());
    }

    /**
     * Add a multiplier to a status given by the statusName, stack additively. Returns the the resultant for the status.
     * @param statusName The name of the status
     * @param multiplier The multiplier to be added. Gets ADDED to the current multiplier.
     * @param duration The duration for this multiplier - any value <= 0 is permanent changes.
     * @return Returns the resultant for this status, or -1234 if it failed.
     */
    public float addStatusMultiplier(String statusName, float multiplier, int duration){

            try {
                if (!statuses.containsKey(statusName)) {
                    float baseValue = StatusConstants.STATUS_VALUES.valueOf(statusName).getBaseValue();
                    addStatus(statusName, baseValue);
                }

                if(duration<=0){
                    statuses.get(statusName).addPermanentMultiplier(multiplier);
                }else{
                    statuses.get(statusName).addTickingMultiplier(duration,multiplier);
                }
                return statuses.get(statusName).getResultant();
            } catch (Exception e) {
                Gdx.app.error("ADD_STATUS", "This status (" + statusName+ ") is not yet initiated, and no known base value is known", e);
                e.printStackTrace();
            }


        //If non-successful - returns -1234
        return -1234;

    }

    /**
     * Add an addition to a status given by the statusName. Returns the resultant for the status.
     * @param statusName The name of the status.
     * @param change The addition to be added.
     * @param duration The duration for this addition - any value <= 0 is permanent changes.
     * @return Returns the resultant for this status, or -1234 if it failed.
     */
    public float addStatusAddition(String statusName, float change, int duration) {



        try {
            if (!statuses.containsKey(statusName)) {
                float baseValue = StatusConstants.STATUS_VALUES.valueOf(statusName).getBaseValue();
                addStatus(statusName, baseValue);
            }

            if(duration<=0){
                statuses.get(statusName).addPermanentAddition(change);
            }else{
                statuses.get(statusName).addTickingAddition(duration,change);
            }

            return statuses.get(statusName).getResultant();

        } catch (Exception e) {
            Gdx.app.error("ADD_STATUS", "This status (" + statusName+ ") is not yet initiated, and no known base value is known", e);
            e.printStackTrace();
        }

        //If non-successful, return -1234
        return -1234;
    }

    /**
     * Gets all statuses and their resulting Float resultants.
     * @return Returns all statuses and their resulting Float resultants.
     */
    public HashMap<String,Float> getAllResultants(){
        HashMap<String,Float> result = new HashMap<>();
        for (String key : statuses.keySet()) {
            result.put(key, statuses.get(key).getResultant());
        }
        return result;
    }


    /**
     * Run the effect code related to the status with name statusName
     * @param statusName The name of the status whose effect should run.
     */
    void parseEffects(String statusName){
        String[] effectArray = StatusConstants.STATUS_VALUES.valueOf(statusName).getEffect();

        float value;
        if(effectArray[2].equalsIgnoreCase("RESULTANT")){
            value = statuses.get(statusName).getResultant();
        }else {
            value = Float.parseFloat(effectArray[2]);
        }

        if (!statuses.get(statusName).isPlayedThisTurn()) {
            switch (effectArray[1]) {
                case "ADD":
                    addStatusAddition(effectArray[0], value * Float.parseFloat(effectArray[4]), Integer.parseInt(effectArray[3]));
                    break;
                case "MULT":
                    addStatusMultiplier(effectArray[0], value * Float.parseFloat(effectArray[4]), Integer.parseInt(effectArray[3]));
                    break;
                case "SET":
                    //TODO: For now, we can only set to 0
                    addStatusMultiplier(effectArray[0], -statuses.get(statusName).getTotalMultipliers(), Integer.parseInt(effectArray[3]));
                    break;
            }

            statuses.get(statusName).play();
        }
    }

    /**
     * Runs all effects currently handled bt this StatusHandler. Only the effects corresponding to the timing will run.
     * @param timing The timing for when this is run (TURN_START, CARD_PLAYED, TURN_END)
     */
    public void runEffects(TIMING_TYPE timing){
        for (String key: statuses.keySet()) {
            if(!statuses.get(key).playedThisTurn){

                //This status has is a base status or a lookup status
                if (StatusConstants.STATUS_VALUES.valueOf(key).getEffect() == null)
                    continue;

                //This status has no resultant - ignore it.
                //TODO: Will this have to be fixed?
                if(statuses.get(key).getResultant()<=0)
                    continue;

                String effectTiming = StatusConstants.STATUS_VALUES.valueOf(key).getTiming();
                switch (timing) {
                    case TURN_START:
                        if(effectTiming.equalsIgnoreCase("TURN_START") || effectTiming.equalsIgnoreCase("INSTANT")){
                            parseEffects(key);
                        }
                        break;
                    case CARD_PLAYED:
                        if (effectTiming.equalsIgnoreCase("INSTANT")){
                            parseEffects(key);
                        }
                        break;
                    case TURN_END:
                        if (effectTiming.equalsIgnoreCase("TURN_END") || effectTiming.equalsIgnoreCase("INSTANT")){
                            parseEffects(key);
                        }
                        break;
                }

            }
        }
    }

    /**
     * Updates all ticking modifiers assosiated with this StatusHandler. Call on turn end.
     */
    public void nextTurn(){
        for (Status status : statuses.values()) {

            status.nextTurn();
        }
    }

    /**
     * A class to handle the modifier logic for status effects.
     */
    class Status{

        String name;
        float baseValue;
        ArrayList<float[]> tickMultipliers;
        ArrayList<float[]> tickAdditions;
        float multipliers = 1;
        float additions = 0;
        boolean playedThisTurn = false;

        /**
         * Ititiate the Status with a base value
         * @param baseValue the base value for this status.
         */
        Status(String name, float baseValue){
            this.name = name;
            this.baseValue = baseValue;
            tickAdditions = new ArrayList<>();
            tickMultipliers = new ArrayList<>();
        }

        public boolean isPlayedThisTurn() {
            return playedThisTurn;
        }

        public void play() {
            this.playedThisTurn = true;
        }

        /**
         * Change the base value for this status by an amount.
         * @param change The amount to change the base value with - NOT the new base value.
         * @return returns the new base value.
         */
        float changeBase(float change){
            baseValue+=change;
            return baseValue;
        }

        /**
         * Adds a new permanent multiplier additively.
         * @param multiplier The change to the multiplier.
         * @return Returns the new (permanent) multiplier.
         */
        float addPermanentMultiplier(float multiplier){
            if(multipliers+multiplier<=0){
                //TODO: How should this be handled? Error? Make the base value 0? Make the mulitplier 0.01 (1%)? Because what about health?
            }

            multipliers+=multiplier;
            return multipliers;

        }

        /**
         * Adds a new permanent addition.
         * @param change The change to the additions.
         * @return Returns the new (permanent) additions.
         */
        float addPermanentAddition(float change){
            float value = change;

            if(baseStats.contains(name))
                value = checkAddForBaseStat(change);

            additions+=value;
            return additions;
        }

        /**
         * Adds a new ticking multiplier - aka after the duration has passed, the multiplier will be removed.
         * @param duration The duration of effect.
         * @param multiplier The multiplier to additively add.
         */
        void addTickingMultiplier(int duration, float multiplier){
            tickMultipliers.add(new float[]{duration,multiplier});
        }

        /**
         * Adds a new ticking addition - aka after the duration has passed, the addition will be removed.
         * @param duration The duration of effect.
         * @param change The addition to add.
         */
        void addTickingAddition(int duration, float change){
            float value = change;
            if(baseStats.contains(name))
                value = checkAddForBaseStat(change);

            tickAdditions.add(new float[]{duration, value});
        }

        /**
         * Method to handle additions that relate to base stats. (Primarily health)
         * @param change The change the effect gives.
         * @return The modified value
         */
        float checkAddForBaseStat(float change){
            float value = change;
            float posMin = change/Math.abs(change);
            if(name.equalsIgnoreCase("health")){
                value = change/getStatusResultant("defence_modifier");
                value = Math.min(Math.abs(value),getStatusResultant("max_damage"));
                value*=posMin;
            }

            return value;
        }

        /**
         * Gets the current total multipliers.
         * @return Returns the current total multipliers.
         */
        float getTotalMultipliers(){
            float result = multipliers;
            for (float[] tickMult : tickMultipliers) {
                if(tickMult[0]>0){
                    result+=tickMult[1];
                }
            }
            return result;
        }

        /**
         * Gets the current total additions.
         * @return Returns the current total additions.
         */
        float getTotalAdditions(){
            float result = additions;
            for (float[] tickAdd : tickAdditions) {
                if(tickAdd[0]>0){
                    result+=tickAdd[1];
                }
            }
            return result;
        }

        /**
         * Get the total resultant as a function: (baseValue + totalAdditions)*totalMultipliers
         * @return Returns the total resultant for this turn.
         */
        float getResultant(){
            float result = baseValue;
            result+=getTotalAdditions();
            result*=getTotalMultipliers();
            return result;
        }

        /**
         * Update all ticking modifiers by 1 step.
         */
        void nextTurn(){

            for (int i = tickMultipliers.size()-1; i >= 0; i--) {
                tickMultipliers.get(i)[0]-=1;
                if(tickMultipliers.get(i)[0]<=0){
                    tickMultipliers.remove(i);
                }
            }

            for (int i = tickAdditions.size()-1; i >= 0; i--) {
                tickAdditions.get(i)[0]-=1;
                if(tickAdditions.get(i)[0]<=0){
                    tickAdditions.remove(i);
                }
            }

            playedThisTurn = false;

        }

    }
}
