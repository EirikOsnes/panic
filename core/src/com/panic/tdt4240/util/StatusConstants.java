package com.panic.tdt4240.util;

/**
 * Class to hold available statuses and their base values
 */

public class StatusConstants {

    //TODO: Add a CHECK status type? As is check if a status exists>0?

    public enum StatusValues {
        /* Base values*/
        health(100),
        damage_modifier(1),
        defence_modifier(1),
        movement_modifier(1),
        max_damage(1000),

        /* Flag values */

        laser_pointer(0),

        /* Effect statuses */
        poison(0, new String[]{"health","ADD","RESULTANT","0", "-1"}, "TURN_END"),
        invulnerable(0,new String[]{"max_damage", "SET", "0", "1", "1"}, "INSTANT"),
        antidote(0, new String[]{"poison", "CLEAR", "0", "0", "0"}, "INSTANT"),
        glue(0, new String[]{"movement_modifier", "SET", "0", "1", "0"}, "INSTANT");

        private float baseValue;
        private String[] effect;
        private String timing;

        StatusValues(float baseValue){
            this.baseValue = baseValue;
        }

        /**
         * Alternate setter for status values, where effect is also defined as an array of Strings.
         * Effect is on the format {TARGET_STATUS, ADD/MULT/SET/CLEAR, VALUE (a float value or RESULTANT), DURATION, MULTIPLIER_FOR_VALUE}
         * NOTE: For CLEAR; VALUE, DURATION and MULTIPLIER is ignored.
         * Timing is INSTANT/TURN_START/TURN_END
         * @param baseValue The base value for this status
         * @param effect The string of tags to define the effect.
         * @param timing When the effect will play out
         */
        StatusValues(float baseValue, String[] effect, String timing){
            this.baseValue = baseValue;
            this.effect = effect;
            this.timing = timing;
        }

        public String[] getEffect(){
            return effect;
        }

        public String getTiming() {
            return timing;
        }

        public float getBaseValue(){
            return baseValue;
        }
    }



}
