package com.panic.tdt4240.models;


import com.panic.tdt4240.events.Event;
import com.panic.tdt4240.events.EventFactory;
import com.panic.tdt4240.events.EventListener;
import com.panic.tdt4240.events.EventBus;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.util.StatusHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Asteroid model.
 */


public class Asteroid implements EventListener{
  
    private String id;
    private StatusHandler statusHandler;
    private Sprite sprite;
    private ArrayList<Asteroid> neighbours;
    private Vector2 position;
    private ArrayList<String> vehicleIDs;


    public Asteroid(Sprite sprite) {
        EventBus.getInstance().addListener(this);
        this.sprite = sprite;
        neighbours = new ArrayList<>();
        statusHandler = new StatusHandler(this);
        position = new Vector2();
        vehicleIDs = new ArrayList<>();
    }

    /**
     * connects two asteroids by adding it to the asteroids' neighbour list.
     * @param asteroid The asteroid to become this asteroid's neighbour
     * @return The asteroid given in the input
     */
    public Asteroid connect(Asteroid asteroid){
        neighbours.add(asteroid);
        asteroid.neighbours.add(this);
        return asteroid;
    }

    /**
     * Defines where the asteroid will be placed on any given map. The position is relative, so it will be scaled between 0 and 1
     * @param position The position of the asteroid
     * @return Whether the position is valid or not
     */
    public boolean setPosition(Vector2 position) {
        if((position.x>=0 && position.x<=1) && (position.y>=0 && position.y<=1)) {
            this.position = position;
            return true;
        }
        else{
            return false;
        }
    }

    public void addVehicle(String vehicleID){
        if(!vehicleIDs.contains(vehicleID)) {
            vehicleIDs.add(vehicleID);
        }
    }

    public boolean removeVehicle(String vehicleID){
        if(vehicleIDs.contains(vehicleID)){
            vehicleIDs.remove(vehicleID);
            return true;
        }
        return false;
    }

    /**
     *
     * @return The neighbours of the asteroid
     */
    public ArrayList<Asteroid> getNeighbours() {
        return neighbours;
    }

    /**
     *
     * @param asteroid The asteroid to check for a Connection
     * @return Whether the asteroids are connected or not
     */
    public boolean isConnected(Asteroid asteroid){
        return neighbours.contains(asteroid);
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosition() {
        return position;
    }

    public ArrayList<String> getVehicles() {
        return vehicleIDs;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  
    @Override
    public void handleEvent(Event e) {
        if (e.getT() == Event.Type.MOVE) {
            if (this.vehicleIDs.contains(e.getInstigatorID())) {
                this.removeVehicle(e.getInstigatorID());
            }
            if (e.getTargetID().equals(this.id)) {
                this.addVehicle(e.getInstigatorID());
            }
        }

        else if (e.getT() == Event.Type.ATTACK && e.getTargetID().equals(this.id)) {
            for (String vid : vehicleIDs) {
                if (e.isFriendlyFire() || !vid.equals(e.getInstigatorID())) {
                    EventFactory.postClonedEvent(e, vid);
                }
            }
        }
    }
}
