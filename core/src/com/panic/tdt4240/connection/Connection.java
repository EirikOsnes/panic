package com.panic.tdt4240.connection;

import com.panic.tdt4240.models.Vehicle;

import java.util.ArrayList;

/**
 * Created by Eirik on 09-Apr-18.
 */

public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
    }

    /**
     * A method call to get all the Vehicles for this game. Should be instatiated copies, with ID.
     * @return Returns all the Vehicles for this game.
     */
    public ArrayList<Vehicle> getAllVehicles(){
        ArrayList<Vehicle> result = null;

        return result;
    }

    /**
     * Get the ID of the current players vehicle.
     * @return returns the ID.
     */
    public String getMyVehicle() {
        return null;
    }


    /**
     * Get the map ID for this game
     * @return Returns the map ID.
     */
    public String getMapID() {
        return null;
    }
}
