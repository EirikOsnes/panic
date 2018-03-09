package com.panic.tdt4240.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eirik on 05-Mar-18.
 */

public class Asteroid{
    private HashMap<String,Object> statuses;
    private Sprite sprite;
    private ArrayList<Asteroid> neighbours;
    private Vector2 position;
    private ArrayList<Vehicle> vehicles;


    public Asteroid(Sprite sprite){
        this.sprite = sprite;
        neighbours = new ArrayList<Asteroid>();
        statuses = new HashMap<String, Object>();
        position = new Vector2();
        vehicles = new ArrayList<>();
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
     * Adds a status to the asteroid. The statuses are stored within the status hashmap
     * @param key The identity of the status
     * @param effect The definition of the status to be added
     */
    public void addStatus(String key, Object effect){
        statuses.put(key,effect);
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

    public void addVehicle(Vehicle vehicle){
        if(!vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
        }
    }

    public boolean removeVehicle(Vehicle vehicle){
        if(vehicles.contains(vehicle)){
            vehicles.remove(vehicle);
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
     * @param asteroid The asteroid to check for a connection
     * @return Whether the asteroids are connected or not
     */
    public boolean isConnected(Asteroid asteroid){
        return neighbours.contains(asteroid);
    }

    public HashMap<String, Object> getStatuses() {
        return statuses;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosition() {
        return position;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
