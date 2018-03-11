package com.panic.tdt4240.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Object for the Vehicle units.
 */

public class Vehicle {

    HashMap<String, Status> statuses;

    public void addStatus(String name, float baseValue){

    }

    private class Status{
        float baseValue;
        ArrayList<float[]> tickMultipliers;
        ArrayList<float[]> tickAdditions;
        float multipliers = 1;
        float additions = 0;

        public Status(float baseValue){
            this.baseValue = baseValue;
            tickAdditions = new ArrayList<>();
            tickMultipliers = new ArrayList<>();
        }

        public float changeBase(float change){
            baseValue+=change;
            return baseValue;
        }

        public float addPermanentMultiplier(float multiplier){
            if(multipliers+multiplier<=0){
                //TODO: How should this be handled? Error? Make the base value 0? Make the mulitplier 0.01 (1%)? Because what about health?
            }

            multipliers+=multiplier;
            return multipliers;

        }

        public float addPermanentAddition(float change){
            additions+=change;
            return additions;
        }

        public void addTickingMultiplier(int duration, float multiplier){
            tickMultipliers.add(new float[]{duration,multiplier});
        }

        public void addTickingAddition(int duration, float change){
            tickAdditions.add(new float[]{duration, change});
        }

        public float getTotalMultipliers(){
            float result = multipliers;
            for (float[] tickMult : tickMultipliers) {
                if(tickMult[0]>0){
                    result+=tickMult[1];
                }
            }
            return result;
        }

        public float getTotalAdditions(){
            float result = additions;
            for (float[] tickAdd : tickAdditions) {
                if(tickAdd[0]>0){
                    result+=tickAdd[1];
                }
            }
            return result;
        }

        public float getResultant(){
            float result = baseValue;
            result+=getTotalAdditions();
            result*=getTotalMultipliers();
            return result;
        }

        public void nextTurn(){

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
