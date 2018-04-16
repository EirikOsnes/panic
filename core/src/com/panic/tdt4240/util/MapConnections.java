package com.panic.tdt4240.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.models.Asteroid;

import java.util.ArrayList;

/**
 * Created by Hermann on 15.04.2018.
 */

public class MapConnections {

    private ArrayList<AsteroidConnection> connections;
    private float ScreenWidth;
    private float ScreenHeight;

    public MapConnections(float ScreenWidth, float ScreenHeight){
        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;

        connections = new ArrayList<>();
    }

    /**
     * Adds connection between two asteroids on the map, if it doesn't already exist
     * @param start asteroid id
     * @param end asteroid id
     * @param asteroidWidth width of sprite, for calculating center
     * @param asteroidHeight height of sprite, for calculating center
     * @param tableHeight height of table, for calculating buffer height
     */
    public void addConnection(Asteroid start, Asteroid end, float asteroidWidth, float asteroidHeight, float tableHeight){
        if(notConnected(start.getId(), end.getId())){
            AsteroidConnection connection = new AsteroidConnection(
                    //Calculation of center point of the asteroids, see setUpMap() in PlayCardView
                    new Vector2(start.getPosition().x *(ScreenWidth - asteroidWidth) + asteroidWidth/2,
                            start.getPosition().y *(ScreenHeight - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    new Vector2(end.getPosition().x *(ScreenWidth - asteroidWidth) + asteroidWidth/2,
                            end.getPosition().y *(ScreenHeight - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    start.getId(), end.getId());
            connections.add(connection);
        }
    }

    /**
     * Checks if an equivalent connection has already been added
     * @param startID start asteroid
     * @param endID end asteroid
     * @return whether this connection already exists
     */
    private boolean notConnected(String startID, String endID){
        for(AsteroidConnection connection: connections){
            if(connection.startID.equals(endID) && connection.endID.equals(startID)){
                return false;
            }
            else if(connection.startID.equals(startID) && connection.endID.equals(endID)){
                return false;
            }
        }
        return true;
    }
    /**
     * @return array of start and endpoints of connecting lines between asteroids
     */
    public ArrayList<Vector2[]> getConnections(){
        ArrayList<Vector2[]> lines = new ArrayList<>();
        for(AsteroidConnection connection : connections){
            Vector2[] line = new Vector2[2];
            line[0] = connection.start;
            line[1] = connection.end;
            lines.add(line);
        }
        return lines;
    }

    private class AsteroidConnection{
        /**
         * Class for keeping track of connections between asteroids, for rendering in Views
         * Has id for start and end asteroid, and their coordinates
         */
        private Vector2 start;
        private Vector2 end;
        private String startID;
        private String endID;

        private AsteroidConnection(Vector2 start, Vector2 end, String startID, String endID){
            this.start = start;
            this.startID = startID;
            this.end = end;
            this.endID = endID;
        }
    }
}
