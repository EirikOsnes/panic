package com.panic.tdt4240.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class to hold all statuses and the logic on them.
 */

public class StatusHandler {


    private HashMap<String, Status> statuses;
    private Object parent;


    /**
     * A class to hold all statuses and the logic on them. Passes in the parent for now, in case we want some events to trigger from here.
     * @param parent The object that called this class.
     */
    public StatusHandler(Object parent){
        this.parent = parent;
        statuses = new HashMap<>();
    }

    /**
     * Should ONLY be used when initiating an object from XML file - or if you wish to override an existing status (as it will be wiped).
     * ONLY use if you know what you are doing.
     * @param name The name of the status
     * @param baseValue The base value of the status.
     */
    public void addStatus(String name, float baseValue){
        statuses.put(name, new Status(baseValue));
    }

    /**
     * Add a multiplier to a status given by the statusName, stack additively. Returns the the resultant for the status.
     * @param statusName The name of the status
     * @param multiplier The multiplier to be added. Gets ADDED to the current multiplier.
     * @param duration The duration for this multiplier - any value <= 0 is permanent changes.
     * @return Returns the resultant for this status.
     */
    public float addStatusMultiplier(String statusName, float multiplier, int duration){
        if (!statuses.containsKey(statusName)){
            try {
                throw new Exception("This status (" + statusName+ ") is not yet initiated, and no known base value is known");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: A list over base values? Some other way of handling it?
        }
        if(duration<=0){
            statuses.get(statusName).addPermanentMultiplier(multiplier);
        }else{
            statuses.get(statusName).addTickingMultiplier(duration,multiplier);
        }

        return statuses.get(statusName).getResultant();
    }

    /**
     * Add an addition to a status given by the statusName. Returns the resultant for the status.
     * @param statusName The name of the status.
     * @param change The addition to be added.
     * @param duration The duration for this addition - any value <= 0 is permanent changes.
     * @return Returns the resultant for this status.
     */
    public float addStatusAddition(String statusName, float change, int duration) {
        if (!statuses.containsKey(statusName)){
            try {
                throw new Exception("This status (" + statusName+ ") is not yet initiated, and no known base value is known");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: A list over base values? Some other way of handling it?
        }
        if(duration<=0){
            statuses.get(statusName).addPermanentAddition(change);
        }else{
            statuses.get(statusName).addTickingAddition(duration,change);
        }

        return statuses.get(statusName).getResultant();
    }

    public HashMap<String,Float> getAllResultants(){
        HashMap<String,Float> result = new HashMap<>();
        for (String key : statuses.keySet()) {
            result.put(key, statuses.get(key).getResultant());
        }
        return result;
    }

    public void nextTurn(){
        for (Status status : statuses.values()) {
            status.nextTurn();
        }
    }

    private class Status{
        float baseValue;
        ArrayList<float[]> tickMultipliers;
        ArrayList<float[]> tickAdditions;
        float multipliers = 1;
        float additions = 0;

        Status(float baseValue){
            this.baseValue = baseValue;
            tickAdditions = new ArrayList<>();
            tickMultipliers = new ArrayList<>();
        }

        float changeBase(float change){
            baseValue+=change;
            return baseValue;
        }

        float addPermanentMultiplier(float multiplier){
            if(multipliers+multiplier<=0){
                //TODO: How should this be handled? Error? Make the base value 0? Make the mulitplier 0.01 (1%)? Because what about health?
            }

            multipliers+=multiplier;
            return multipliers;

        }

        float addPermanentAddition(float change){
            additions+=change;
            return additions;
        }

        void addTickingMultiplier(int duration, float multiplier){
            tickMultipliers.add(new float[]{duration,multiplier});
        }

        void addTickingAddition(int duration, float change){
            tickAdditions.add(new float[]{duration, change});
        }

        float getTotalMultipliers(){
            float result = multipliers;
            for (float[] tickMult : tickMultipliers) {
                if(tickMult[0]>0){
                    result+=tickMult[1];
                }
            }
            return result;
        }

        float getTotalAdditions(){
            float result = additions;
            for (float[] tickAdd : tickAdditions) {
                if(tickAdd[0]>0){
                    result+=tickAdd[1];
                }
            }
            return result;
        }

        float getResultant(){
            float result = baseValue;
            result+=getTotalAdditions();
            result*=getTotalMultipliers();
            return result;
        }

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

        }

    }
}
