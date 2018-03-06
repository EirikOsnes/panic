package com.panic.tdt4240.util;

import java.util.ArrayList;

/**
 * Created by magnus on 05.03.2018.
 */

public class Graph {

    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges){
        this.vertices=vertices;
        this.edges=edges;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
}
