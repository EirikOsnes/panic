package com.panic.tdt4240.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Hermann on 14.04.2018.
 */

public class MapMethods {

    /**
     * For an asteroid, gives coordinates within asteroid for each car type
     * Positions are clockwise from lower left section
     * Position of the lower left corner of each section
     */
    public static Vector2 asteroidPositions(float posX, float posY, float width, float height, String colorCar){
        Vector2 position = new Vector2(posX + width/9, posY);
        switch (colorCar){
            case "red_car":
                position.add(0,0);
                break;
            case "green_car":
                position.add(0, height/2+ width/16);
                break;
            case "yellow_car":
                position.add(width/2, height/2 + width/16);
                break;
            case "blue_car":
                position.add(width/2, 0);
                break;
        }
        return position;
    }

}
