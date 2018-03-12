package com.panic.tdt4240.models;

import com.panic.tdt4240.util.Dijkstra;
import com.panic.tdt4240.util.Edge;
import com.panic.tdt4240.util.Graph;
import com.panic.tdt4240.util.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by magnus on 05.03.2018.
 */

public class Map {
    private ArrayList<Asteroid> asteroids;
    private int[][] adjacency;
    HashMap<String, Vehicle> vehicles;

    /**
     * creates a Map object containing a set of asteroids
     * @param asteroids ArrayList of asteroids
     */
    public Map(ArrayList<Asteroid> asteroids){
        vehicles = new HashMap<>();
        this.asteroids = asteroids;
    }

    /**
     * creates a connection between two asteroids
     * @param a1 first asteroid
     * @param a2 second asteroid
     */
    public void connectAsteroids(Asteroid a1,Asteroid a2){
        a1.connect(a2);
    }

    /**
     * adds a vehicle to the vehicle hashmap with the player name as a key
     * @param name of the player
     * @param vehicle to be added
     */
    public void addVehicle(String name, Vehicle vehicle){
        if(!vehicles.containsKey(name)) {
            vehicles.put(name, vehicle);
        }
    }

    /**
     * returns all vehicles on an asteroid. returns null if asteroid does not exist or is empty
     * @param asteroid asteroid to get vehicles from
     * @return all vehicles present on the asteroid
     */
    public ArrayList<String> getVehiclesOnAsteroid(Asteroid asteroid){
        if(asteroid!=null){
            if(!asteroid.getVehicles().isEmpty()) {
                return asteroid.getVehicles();
            }
        }
        return null;
    }

    /**
     * Adds a vehicle to the designated asteroid
     * @param asteroid The asteroid to move the vehicle to
     * @param vehicle The vehicle to be moved
     */
    protected void addVehicleToAsteroid(Asteroid asteroid, Vehicle vehicle){
        asteroid.addVehicle(vehicle.getVehicleID());
    }

    /**
     * Removes a vehicle from the designated asteroid if it is situated on the asteroid
     * @param asteroid The asteroid the vehicle is on
     * @param vehicle The vehicle to be removed
     */
    protected boolean removeVehicleFromAsteroid(Asteroid asteroid, Vehicle vehicle){
        return asteroid.removeVehicle(vehicle.getVehicleID());
    }


    /**
     * Moves a vehicle to a new asteroid
     * @param origin current asteroid of the vehicle
     * @param target vehicle to be moved to
     * @param vehicle vehicle to be moved
     */
    protected void moveVehicle(Asteroid origin, Asteroid target, Vehicle vehicle){
        if(removeVehicleFromAsteroid(origin,vehicle)) {
            addVehicleToAsteroid(target, vehicle);
        }
    }

    /**
     * Removes a vehicle from the game
     * @param name key of the vehicle to be removed
     */
    public void removeVehicle(String name){
        if(vehicles.containsKey(name)){
            vehicles.remove(name);
        }
    }

    /**
     * generates an adjacency matrix based on the asteroids on the map and its connections.
     */
    public void generateAdjacencyMatrix(){
        int[][] matrix = new int[asteroids.size()][asteroids.size()];
        Graph graph = generateVertexAndEdge();
        Dijkstra dijkstra = new Dijkstra(graph);
        for(int i=0;i<asteroids.size();i++) {
            dijkstra.execute(graph.getVertices().get(i));
            for(int j=i;j<asteroids.size();j++){
                if(i!=j){
                    matrix[i][j] = dijkstra.getDistances().get(graph.getVertices().get(j));
                    matrix[j][i] = dijkstra.getDistances().get(graph.getVertices().get(j));
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }
        adjacency = matrix;
    }

    /**
     *
     * @return The adjacency matrix of the map
     */
    public int[][] getAdjacency() {
        return adjacency;
    }

    /**
     * translates the given asteroids and its connections into a graph consisting of vertices and edges.
     * @return The graph representing the asteroids
     */
    protected Graph generateVertexAndEdge(){
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Vertex> vertices = new ArrayList<>();
        for(int i=0;i<asteroids.size();i++) {
            vertices.add(new Vertex(i));
        }
        for(int i=0;i<asteroids.size();i++){
            Asteroid a1=asteroids.get(i);
            for(int j=i;j<asteroids.size();j++){
                Asteroid a2=asteroids.get(j);
                if(a1!=a2) {
                    if (a1.isConnected(a2)){
                        edges.add(new Edge(vertices.get(i), vertices.get(j),1));
                        edges.add(new Edge(vertices.get(j),vertices.get(i),1));
                    }
                }
            }
        }
        for(Edge edge:edges) {
            System.out.println(edge.getSource().toString() + " " + edge.getDestination().toString());
        }
        return new Graph(vertices, edges);
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
}
