package com.panic.tdt4240.util;

import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.models.Asteroid;

import java.util.ArrayList;

/**
 * Created by Hermann on 14.04.2018.
 */

public class MapMethods {

    /**
     * For an asteroid, gives coordinates within asteroid for each car type
     * Positions are clockwise from lower left section
     * Position of the lower left corner of each section
     */
    public static Vector2 asteroidPositions(float posX, float posY, float width, float height, String carColor){
        Vector2 position = new Vector2(posX + width/9, posY);
        switch (carColor.toLowerCase()){
            case "red":
                position.add(0,0);
                break;
            case "green":
                position.add(0, height/2+ width/16);
                break;
            case "yellow":
                position.add(width/2, height/2 + width/16);
                break;
            case "blue":
                position.add(width/2, 0);
                break;
        }
        return position;
    }

    /**
     *
     * @param asteroid asteroid we find vehicles on
     * @param index asteroid index
     * @return array with vehicle, asteroid id and asteroid index
     */
    public static ArrayList<String[]> getVehiclesOnAsteroid(Asteroid asteroid, int index){
        ArrayList<String[]> vehicleOnAsteroid = new ArrayList<>();
        for (int j = 0; j < asteroid.getVehicles().size(); j++) {
            String[] onAsteroid = new String[3];
            onAsteroid[0] = asteroid.getVehicles().get(j);
            onAsteroid[1] = asteroid.getId();
            onAsteroid[2] = index + "";
            vehicleOnAsteroid.add(onAsteroid);
        }
        return vehicleOnAsteroid;
    }

}
