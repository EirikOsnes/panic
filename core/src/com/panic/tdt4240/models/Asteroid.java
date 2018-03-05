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

    public Asteroid(Sprite sprite,ArrayList<Asteroid> neighbours){
        this.sprite=sprite;
        if(neighbours!=null){
            this.neighbours=neighbours;
        }
        else{
            this.neighbours=new ArrayList<Asteroid>();
        }
        statuses = new HashMap<String, Object>();
        position = new Vector2();
    }
    public Asteroid(Sprite sprite){
        this.sprite = sprite;
        neighbours = new ArrayList<Asteroid>();
        statuses = new HashMap<String, Object>();
        position = new Vector2();
    }

    public Asteroid connect(Asteroid asteroid){
        neighbours.add(asteroid);
        asteroid.neighbours.add(this);
        return asteroid;
    }

    public void addStatus(String key, Object effect){
        statuses.put(key,effect);
    }

    public boolean setPosition(Vector2 position) {
        if((position.x>=0 && position.x<=1) && (position.y>=0 && position.y<=1)) {
            this.position = position;
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Asteroid> getNeighbours() {
        return neighbours;
    }

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
}
