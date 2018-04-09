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
    private Player player;

    GameModelHolder(){
        this.vehicles = new ArrayList<>();
        this.asteroids = new ArrayList<>();
        this.map = null;
    }

    public static GameModelHolder getInstance(){
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public String locateVehicle(String vehicleID){
        for (Asteroid asteroid : asteroids) {
            if(asteroid.getVehicles().contains(vehicleID)){
                return asteroid.getId();
            }
        }
        return null;
    }

    public void reset(){
        gmh = new GameModelHolder();
    }
}
