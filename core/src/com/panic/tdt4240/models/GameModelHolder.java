package com.panic.tdt4240.models;

import java.util.ArrayList;

/**
 * Created by Eirik on 07-Apr-18.
 */

public class GameModelHolder {

    private static GameModelHolder gmh;

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Asteroid> asteroids;
    private Map map;

    GameModelHolder(){
        this.vehicles = new ArrayList<>();
        this.asteroids = new ArrayList<>();
    }

    public GameModelHolder getInstance(){
        if(gmh == null){
            gmh = new GameModelHolder();
        }
        return gmh;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void addVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }

    public void setAsteroids(ArrayList<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public Map getMap() {
        return map;
    }
}
