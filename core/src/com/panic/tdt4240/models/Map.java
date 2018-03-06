package com.panic.tdt4240.models;

import com.panic.tdt4240.util.Dijkstra;
import com.panic.tdt4240.util.Edge;
import com.panic.tdt4240.util.Graph;
import com.panic.tdt4240.util.Vertex;

import java.util.ArrayList;

/**
 * Created by magnus on 05.03.2018.
 */

public class Map {
    private ArrayList<Asteroid> asteroids;
    private int[][] adjacency;

    public void connectAsteroids(Asteroid a1,Asteroid a2){
        a1.connect(a2);
    }

    public int[][] generateAdjacencyMatrix(){
        int[][] matrix = new int[asteroids.size()][asteroids.size()];
        Graph graph = generateVertexAndEdge();
        Dijkstra dijkstra = new Dijkstra(graph);
        for(int i=0;i<asteroids.size();i++) {
            dijkstra.execute(graph.getVertices().get(i));

        }
        return matrix;
    }
    private Graph generateVertexAndEdge(){
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        for(int i=0;i<asteroids.size();i++) {
            vertices.add(new Vertex(i));
        }
        for(int i=0;i<asteroids.size();i++){
            Asteroid a1=asteroids.get(i);
            for(int j=0;j<asteroids.size();j++){
                Asteroid a2=asteroids.get(j);
                if(a1!=a2) {
                    if (a1.isConnected(a2)){
                        edges.add(new Edge(vertices.get(i), vertices.get(j),1));
                    }
                }
            }
        }
        return new Graph(vertices, edges);
    }
}
