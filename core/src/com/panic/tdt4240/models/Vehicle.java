package com.panic.tdt4240.models;

import com.panic.tdt4240.util.StatusHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Object for the Vehicle units.
 */

public class Vehicle {

    private StatusHandler statusHandler;

    public Vehicle(){
        statusHandler = new StatusHandler(this);
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }
}
